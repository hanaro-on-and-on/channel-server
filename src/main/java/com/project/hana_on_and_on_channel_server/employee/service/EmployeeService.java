package com.project.hana_on_and_on_channel_server.employee.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeDuplicatedException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomAttendanceMemoRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractsRepository employmentContractsRepository;
    private final CustomWorkPlaceRepository customWorkPlaceRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final CustomAttendanceMemoRepository customAttendanceMemoRepository;

    @Transactional(readOnly = true)
    public EmployeeWorkPlaceInvitationListGetResponse getWorkPlacesInvitations(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // 휴대폰 번호로, 전자서명 안 된 계약서 찾기
        List<EmploymentContract> employmentContracts = employmentContractsRepository.findByEmployeePhoneAndEmployeeSign(employee.getPhoneNumber(), false);

        List<EmployeeWorkPlaceInvitationGetResponse> employeeWorkPlaceInvitationGetResponseList = employmentContracts.stream()
                .map(contract -> {
                    WorkPlaceEmployee workPlaceEmployee = contract.getWorkPlaceEmployee();
                    if (workPlaceEmployee == null) {
                        throw new WorkPlaceEmployeeNotFoundException();
                    }
                    WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
                    if (workPlace == null) {
                        throw new WorkPlaceNotFoundException();
                    }
                    Owner owner = workPlace.getOwner();
                    if (owner == null) {
                        throw new OwnerNotFoundException();
                    }
                    return new EmployeeWorkPlaceInvitationGetResponse(
                            userId,
                            workPlace.getWorkPlaceNm(),
                            workPlace.getColorType(),
                            owner.getOwnerNm()
                    );
                })
                .toList();

        return EmployeeWorkPlaceInvitationListGetResponse.fromEntity(employeeWorkPlaceInvitationGetResponseList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeWorkPlaceCustomCreateResponse createCustomWorkPlaces(Long userId, EmployeeWorkPlaceCustomCreateRequest employeeWorkPlaceCustomCreateRequest) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // customWorkPlace 등록
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.save(CustomWorkPlace.builder()
                        .employee(employee)
                        .customWorkPlaceNm(employeeWorkPlaceCustomCreateRequest.customWorkPlaceNm())
                        .payPerHour(employeeWorkPlaceCustomCreateRequest.payPerHour())
                .build());
        return EmployeeWorkPlaceCustomCreateResponse.fromEntity(customWorkPlace);
    }

    @Transactional(readOnly = true)
    public EmployeeSalaryListGetResponse getSalaries(Long userId, Integer year, Integer month) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // year + month => yyyymm
        String searchDate = String.format("%04d%02d", year, month);

        // attendance + CustomAttendanceMemo
        List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList = new ArrayList<>();

        // 연결근무지 : attendance 모두 찾기
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(
                    workPlaceEmployee, searchDate
            );

            int payment = attendanceList.stream()
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getPayPerHour()))
                    .sum();

            employeeSalaryGetResponseList.add(
                    new EmployeeSalaryGetResponse(
                            true,
                            workPlaceEmployee.getWorkPlaceEmployeeId(),
                            workPlaceEmployee.getEmployeeStatus() == EmployeeStatus.QUIT,
                            workPlaceEmployee.getWorkPlace().getWorkPlaceNm(),
                            workPlaceEmployee.getColorType(),
                            payment
                    )
            );
        }

        // 수동근무지 : CustomAttendanceMemo 모두 찾기
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);

        for (CustomWorkPlace customWorkPlace : customWorkPlaceList) {
            List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndAndAttendDateStartingWith(
                    customWorkPlace, searchDate
            );

            int payment = customAttendanceMemoList.stream()
                    .mapToInt(customAttendanceMemo -> calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getPayPerHour()))
                    .sum();

            employeeSalaryGetResponseList.add(
                    new EmployeeSalaryGetResponse(
                            false,
                            customWorkPlace.getCustomWorkPlaceId(),
                            customWorkPlace.getEmployeeStatus() == EmployeeStatus.QUIT,
                            customWorkPlace.getCustomWorkPlaceNm(),
                            customWorkPlace.getColorType(),
                            payment
                    )
            );
        }

        // totalPayment 계산
        Integer totalPayment = employeeSalaryGetResponseList.stream()
                .mapToInt(EmployeeSalaryGetResponse::payment)
                .sum();

        return EmployeeSalaryListGetResponse.fromEntity(year, month, totalPayment, employeeSalaryGetResponseList);
    }

    @Transactional(readOnly = true)
    public EmployeeSalaryCalendarListGetResponse getCalendarSalaries(Long userId, Integer year, Integer month) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // year + month => yyyymm
        String searchDate = String.format("%04d%02d", year, month);

        // attendance + CustomAttendanceMemo
        List<EmployeeSalaryCalendarGetResponse> employeeSalaryCalendarGetResponseList = new ArrayList<>();

        // 연결근무지 : attendance 모두 찾기
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(
                    workPlaceEmployee, searchDate
            );

            for (Attendance attendance : attendanceList) {
                AttendanceType attendanceType = attendance.getAttendanceType();
                int payment = calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getPayPerHour());
                employeeSalaryCalendarGetResponseList.add(
                        new EmployeeSalaryCalendarGetResponse(
                                true,
                                workPlaceEmployee.getWorkPlace().getWorkPlaceNm(),
                                workPlaceEmployee.getColorType(),
                                attendance.getAttendDate(),
                                attendanceType,
                                attendanceType == AttendanceType.REAL ? attendance.getRealStartTime() : attendance.getRealEndTime(),
                                attendanceType == AttendanceType.EXPECT ? attendance.getStartTime() : attendance.getEndTime(),
                                attendance.getStartTime(),  // TODO 휴게로 변경
                                attendance.getEndTime(),    // TODO
                                payment
                        )
                );
            }
        }

        // 수동근무지 : CustomAttendanceMemo 모두 찾기
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);

        for (CustomWorkPlace customWorkPlace : customWorkPlaceList) {
            List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndAndAttendDateStartingWith(
                    customWorkPlace, searchDate
            );

            int payment = customAttendanceMemoList.stream()
                    .mapToInt(customAttendanceMemo -> calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getPayPerHour()))
                    .sum();

            // TODO
//            employeeSalaryCalendarGetResponseList.add(
//                    new EmployeeSalaryCalendarGetResponse(
//                            false,
//                            customWorkPlace.getCustomWorkPlaceId(),
//                            customWorkPlace.getEmployeeStatus() == EmployeeStatus.QUIT,
//                            customWorkPlace.getCustomWorkPlaceNm(),
//                            customWorkPlace.getColorType(),
//                            payment
//                    )
//            );
        }

        // totalPayment 계산
        Integer totalPayment = employeeSalaryCalendarGetResponseList.stream()
                .mapToInt(EmployeeSalaryCalendarGetResponse::payment)
                .sum();

        return EmployeeSalaryCalendarListGetResponse.fromEntity(totalPayment, totalPayment, totalPayment, totalPayment, employeeSalaryCalendarGetResponseList); // TODO
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAccountUpsertResponse registerEmployeeAccount(Long userId, EmployeeAccountRegRequest employeeAccountRegRequest){
        // employee 존재 여부 확인
        employeeRepository.findByUserId(userId).ifPresent(employee -> {
            throw new EmployeeDuplicatedException();
        });

        // employee 등록
        Employee employee = employeeRepository.save(Employee.builder()
                .userId(userId)
                .accountNumber(employeeAccountRegRequest.accountNumber())
                .employeeNm(employeeAccountRegRequest.employeeNm())
                .build());
        return EmployeeAccountUpsertResponse.fromEntity(employee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmployeeAccount(Long userId, EmployeeAccountUpsertRequest employeeAccountUpsertRequest){
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);
        employee.registerAccountNumber(employeeAccountUpsertRequest.accountNumber());
    }
}
