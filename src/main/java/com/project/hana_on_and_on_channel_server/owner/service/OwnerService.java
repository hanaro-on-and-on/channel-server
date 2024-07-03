package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.dto.*;
import com.project.hana_on_and_on_channel_server.owner.exception.*;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;
import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDFormat;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final WorkPlaceRepository workPlaceRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public OwnerAccountUpsertResponse registerOwnerAccount(Long userId, OwnerAccountUpsertRequest dto) {
        ownerRepository.findByUserIdAndAccountNumber(userId, dto.accountNumber()).ifPresent(owner -> {
            throw new OwnerDuplicatedException();
        });
        Owner owner = ownerRepository.save(Owner.builder()
            .userId(userId)
            .accountNumber(dto.accountNumber())
            .ownerNm(dto.ownerNm())
            .build());
        return OwnerAccountUpsertResponse.fromEntity(owner);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOwnerAccount(Long userId, OwnerAccountUpsertRequest dto) {
        Owner owner = ownerRepository.findByUserIdAndAccountNumber(userId, dto.accountNumber()).orElseThrow(OwnerNotFoundException::new);
        owner.registerAccountNumber(dto.accountNumber());
    }


    @Transactional(readOnly = true)
    public OwnerSalaryWorkPlaceListGetResponse getSalaries(Long userId, Integer year, Integer month) {
        // owner 존재 여부 확인
        Owner owner = ownerRepository.findByUserId(userId).orElseThrow(OwnerNotFoundException::new);

        List<OwnerSalaryEmployeeListGetResponse> ownerSalaryEmployeeListGetResponseList = new ArrayList<>();

        // workplace의 employee별로 attendance 모두 찾기
        List<WorkPlace> workPlaceList = workPlaceRepository.findByOwnerOwnerId(owner.getOwnerId());
        for (WorkPlace workPlace : workPlaceList) {
            // 사업장의 직원별로 attendance 모두 찾기
            ownerSalaryEmployeeListGetResponseList.add(
                    getWorkPlaceSalaries(userId, workPlace.getWorkPlaceId(), year, month)
            );
        }

        // totalPayment 계산
        Integer totalPayment = ownerSalaryEmployeeListGetResponseList.stream()
                .mapToInt(OwnerSalaryEmployeeListGetResponse::payment)
                .sum();

        return OwnerSalaryWorkPlaceListGetResponse.fromEntity(year, month, totalPayment, ownerSalaryEmployeeListGetResponseList);
    }

    @Transactional(readOnly = true)
    public OwnerSalaryEmployeeListGetResponse getWorkPlaceSalaries(Long userId, Long workPlaceId, Integer year, Integer month) {
        // owner 일치 여부 확인
        // TODO

        // workPlace 존재 여부 확인
        WorkPlace workPlace = workPlaceRepository.findById(workPlaceId).orElseThrow(WorkPlaceNotFoundException::new);

        // year + month => yyyymm
        String searchDate = String.format("%04d%02d", year, month);

        // workplace의 employee별로 attendance 모두 찾기
        List<OwnerSalaryEmployeeGetResponse> ownerSalaryEmployeeGetResponseList = new ArrayList<>();

        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByWorkPlaceWorkPlaceId(workPlaceId);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(
                    workPlaceEmployee, searchDate
            );

            int payment = attendanceList.stream()
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour()))
                    .sum();

            ownerSalaryEmployeeGetResponseList.add(
                    new OwnerSalaryEmployeeGetResponse(
                            workPlaceEmployee.getWorkPlaceEmployeeId(),
                            workPlaceEmployee.getEmployee().getEmployeeNm(),
                            workPlaceEmployee.getWorkStartDate(),
                            payment
                    )
            );
        }

        // workplace의 totalPayment 계산
        Integer totalPayment = ownerSalaryEmployeeGetResponseList.stream()
                .mapToInt(OwnerSalaryEmployeeGetResponse::payment)
                .sum();

        return OwnerSalaryEmployeeListGetResponse.fromEntity(workPlaceId, workPlace.getWorkPlaceNm(), workPlace.getColorType(), totalPayment, ownerSalaryEmployeeGetResponseList);
    }

    @Transactional(readOnly = true)
    public OwnerSalaryCalendarWorkPlaceListGetResponse getCalendarSalaries(Long userId, Integer year, Integer month) {
        // owner 존재 여부 확인
        Owner owner = ownerRepository.findByUserId(userId).orElseThrow(OwnerNotFoundException::new);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        String yesterDate = String.format("%04d%02d%02d", yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth());
        Integer currentPayment = 0;
        Integer totalPayment = 0;

        // workplace의 employee별로 attendance 모두 찾기
        List<WorkPlace> workPlaceList = workPlaceRepository.findByOwnerOwnerId(owner.getOwnerId());

        List<OwnerSalaryCalendarEmployeeListGetResponse> ownerSalaryCalendarEmployeeListGetResponseList = new ArrayList<>();

        for (WorkPlace workPlace : workPlaceList) {
            List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByWorkPlaceWorkPlaceId(workPlace.getWorkPlaceId());

            Map<String, List<OwnerSalaryCalendarEmployeeGetResponse>> dailyEmployeeResponses = new HashMap<>();

            for (int day = 1; day <= LocalDate.of(year, month, 1).lengthOfMonth(); day++) {
                String searchDate = String.format("%04d%02d%02d", year, month, day);

                for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
                    Attendance attendance = attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(workPlaceEmployee.getWorkPlaceEmployeeId(), searchDate)
                            .orElse(null);
                    if (attendance == null) {
                        continue;
                    }

                    int payment = calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour());
                    OwnerSalaryCalendarEmployeeGetResponse employeeResponse = new OwnerSalaryCalendarEmployeeGetResponse(
                            attendance.getAttendanceId(),
                            workPlaceEmployee.getEmployee().getEmployeeNm(),
                            attendance.getStartTime(),
                            attendance.getEndTime(),
                            attendance.getRestMinute(),
                            payment
                    );

                    dailyEmployeeResponses.computeIfAbsent(searchDate, k -> new ArrayList<>())
                            .add(employeeResponse);
                }
            }

            for (Map.Entry<String, List<OwnerSalaryCalendarEmployeeGetResponse>> entry : dailyEmployeeResponses.entrySet()) {
                String attendDate = entry.getKey();
                List<OwnerSalaryCalendarEmployeeGetResponse> employeeResponses = entry.getValue();

                int workPlacePayment = employeeResponses.stream()
                        .mapToInt(OwnerSalaryCalendarEmployeeGetResponse::payment)
                        .sum();

                if (attendDate.compareTo(yesterDate) <= 0) {    // 1일~어제 까지
                    currentPayment += workPlacePayment;
                }
                totalPayment += workPlacePayment;

                ownerSalaryCalendarEmployeeListGetResponseList.add(new OwnerSalaryCalendarEmployeeListGetResponse(
                        workPlace.getWorkPlaceNm(),
                        workPlace.getColorType().getCode(),
                        attendDate,
                        workPlacePayment,
                        employeeResponses.size(),
                        employeeResponses
                ));
            }
        }

        // attendDate 순 정렬
        Collections.sort(ownerSalaryCalendarEmployeeListGetResponseList, Comparator.comparing(OwnerSalaryCalendarEmployeeListGetResponse::attendDate));

        return OwnerSalaryCalendarWorkPlaceListGetResponse.fromEntity(
                currentPayment,
                totalPayment,
                ownerSalaryCalendarEmployeeListGetResponseList
        );
    }

    @Transactional(readOnly = true)
    public OwnerAttendanceGetResponse findAttendance(Long userId, Long attendanceId){
        Owner owner = ownerRepository.findByUserId(userId)
                .orElseThrow(OwnerNotFoundException::new);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(AttendanceNotFoundException::new);

        // 사용자 검증
        if(userId != attendance.getWorkPlaceEmployee().getWorkPlace().getOwner().getUserId()){
            throw new OwnerInvalidException(userId);
        }

        return OwnerAttendanceGetResponse.fromEntity(attendance);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OwnerAttendanceUpsertResponse saveAttendance(Long userId, OwnerAttendanceUpsertRequest request){
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(request.workPlaceEmployeeId())
                .orElseThrow(WorkPlaceEmployeeNotFoundException::new);

        if(userId != workPlaceEmployee.getWorkPlace().getOwner().getUserId()){
            throw new OwnerInvalidException();
        }

        boolean isReal = request.endTime().isBefore(LocalDateTime.now());

        Attendance attendance = Attendance.builder()
                .workPlaceEmployee(workPlaceEmployee)
                .attendanceType(isReal? AttendanceType.REAL : AttendanceType.EXPECT)
                .payPerHour(request.payPerHour())
                .attendDate(localDateTimeToYMDFormat(request.startTime()))
                .startTime(request.startTime())
                .endTime(request.endTime())
                .realStartTime(isReal ? request.startTime() : null)
                .realEndTime(isReal ? request.endTime() : null)
                .restMinute(request.restMinute())
                .build();

        attendanceRepository.save(attendance);
        return new OwnerAttendanceUpsertResponse(attendance.getAttendanceId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OwnerAttendanceUpsertResponse updateAttendance(Long userId, Long attendanceId, OwnerAttendanceUpsertRequest request){
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(request.workPlaceEmployeeId())
                .orElseThrow(WorkPlaceEmployeeNotFoundException::new);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(AttendanceNotFoundException::new);

        //해당 사업장의 근무자가 아닐 경우 예외 처리
        if(attendance.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceId() != workPlaceEmployee.getWorkPlace().getWorkPlaceId()){
            throw new WorkPlaceEmployeeInvalidException(workPlaceEmployee.getWorkPlaceEmployeeId());
        }

        //사장님 확인
        if(userId != workPlaceEmployee.getWorkPlace().getOwner().getUserId()){
            throw new OwnerInvalidException();
        }

        boolean isReal = request.endTime().isBefore(LocalDateTime.now());

        attendance.modifyAttendance(
                workPlaceEmployee,
                isReal ? AttendanceType.REAL : AttendanceType.EXPECT,
                request.payPerHour(),
                localDateTimeToYMDFormat(request.startTime()),
                request.startTime(),
                request.endTime(),
                isReal ? request.startTime() : null,
                isReal ? request.endTime() : null,
                request.restMinute()
        );

        return new OwnerAttendanceUpsertResponse(attendance.getAttendanceId());
    }
}
