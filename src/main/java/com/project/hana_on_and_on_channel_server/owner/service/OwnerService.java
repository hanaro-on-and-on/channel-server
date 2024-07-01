package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeSalaryListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.dto.*;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerDuplicatedException;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;

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
        List<OwnerSalaryGetResponse> ownerSalaryGetResponseList = new ArrayList<>();

        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByWorkPlaceWorkPlaceId(workPlaceId);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(
                    workPlaceEmployee, searchDate
            );

            int payment = attendanceList.stream()
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getPayPerHour()))
                    .sum();

            ownerSalaryGetResponseList.add(
                    new OwnerSalaryGetResponse(
                            workPlaceEmployee.getWorkPlaceEmployeeId(),
                            workPlaceEmployee.getEmployee().getEmployeeNm(),
                            workPlaceEmployee.getWorkStartDate(),
                            payment
                    )
            );
        }

        // workplace의 totalPayment 계산
        Integer totalPayment = ownerSalaryGetResponseList.stream()
                .mapToInt(OwnerSalaryGetResponse::payment)
                .sum();

        return OwnerSalaryEmployeeListGetResponse.fromEntity(workPlaceId, workPlace.getWorkPlaceNm(), workPlace.getColorType(), totalPayment, ownerSalaryGetResponseList);
    }
}
