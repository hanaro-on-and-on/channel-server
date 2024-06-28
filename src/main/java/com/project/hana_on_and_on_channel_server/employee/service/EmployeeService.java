package com.project.hana_on_and_on_channel_server.employee.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
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

        // year-month에 해당하는 LocalDateTime 찾기
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        // attendance + CustomAttendanceMemo
        List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList = new ArrayList<>();

        // 연결근무지 : attendance 모두 찾기
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndCreatedAtBetween(
                    workPlaceEmployee, startDateTime, endDateTime
            );

            int payment = attendanceList.stream()
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getStartTime(), attendance.getEndTime(), attendance.getPayPerHour()))
                    .sum();

            employeeSalaryGetResponseList.add(
                    new EmployeeSalaryGetResponse(
                            workPlaceEmployee.getEmployeeStatus() == EmployeeStatus.QUIT,
                            workPlaceEmployee.getWorkPlace().getWorkPlaceNm(),
                            payment
                    )
            );
        }

        // 수동근무지 : CustomAttendanceMemo 모두 찾기
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);

        for (CustomWorkPlace customWorkPlace : customWorkPlaceList) {
            List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndCreatedAtBetween(
                    customWorkPlace, startDateTime, endDateTime
            );

            int payment = customAttendanceMemoList.stream()
                    .mapToInt(customAttendanceMemo -> calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getPayPerHour()))
                    .sum();

            employeeSalaryGetResponseList.add(
                    new EmployeeSalaryGetResponse(
                            customWorkPlace.getEmployeeStatus() == EmployeeStatus.QUIT,
                            customWorkPlace.getCustomWorkPlaceNm(),
                            payment
                    )
            );
        }

        // totalPayment 계산
        Integer totalPayment = employeeSalaryGetResponseList.stream()
                .mapToInt(EmployeeSalaryGetResponse::payment)
                .sum();

        return EmployeeSalaryListGetResponse.fromEntity(month, totalPayment, employeeSalaryGetResponseList);
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
