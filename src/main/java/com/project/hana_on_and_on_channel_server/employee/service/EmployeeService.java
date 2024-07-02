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
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractRepository employmentContractRepository;
    private final CustomWorkPlaceRepository customWorkPlaceRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final CustomAttendanceMemoRepository customAttendanceMemoRepository;

    @Transactional(readOnly = true)
    public EmployeeWorkPlaceListGetResponse getWorkPlaces(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // 1. 초대된 사업장 : 휴대폰 번호로, 전자서명 안 된 계약서 찾기
        List<EmploymentContract> employmentContractList = employmentContractRepository.findByEmployeePhoneAndEmployeeSign(employee.getPhoneNumber(), false);
        List<EmployeeWorkPlaceGetResponse> invitatedWorkPlaceGetResponseList = employmentContractList.stream()
                .map(employmentContract -> {
                    WorkPlaceEmployee workPlaceEmployee = employmentContract.getWorkPlaceEmployee();
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
                    return new EmployeeWorkPlaceGetResponse(
                            workPlaceEmployee.getDeletedYn(),
                            userId,
                            employmentContract.getEmploymentContractId(),
                            workPlace.getWorkPlaceNm(),
                            workPlace.getColorType(),
                            owner.getOwnerNm()
                    );
                })
                .toList();

        // 2. 연결된 사업장
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);
        List<EmployeeWorkPlaceGetResponse> connectedWorkPlaceGetResponseList = workPlaceEmployeeList.stream()
                .map(workPlaceEmployee -> {
                    WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
                    if (workPlace == null) {
                        throw new WorkPlaceNotFoundException();
                    }
                    Owner owner = workPlace.getOwner();
                    if (owner == null) {
                        throw new OwnerNotFoundException();
                    }
                    return new EmployeeWorkPlaceGetResponse(
                            workPlaceEmployee.getDeletedYn(),
                            userId,
                            null,
                            workPlace.getWorkPlaceNm(),
                            workPlace.getColorType(),
                            owner.getOwnerNm()
                    );
                })
                .toList();

        // 3. 수동 사업장
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);
        List<EmployeeWorkPlaceGetResponse> customWorkPlaceGetResponseList = customWorkPlaceList.stream()
                .map(customWorkPlace -> {
                    return new EmployeeWorkPlaceGetResponse(
                            false,
                            userId,
                            null,
                            customWorkPlace.getCustomWorkPlaceNm(),
                            customWorkPlace.getColorType(),
                            null
                    );
                })
                .toList();

        return EmployeeWorkPlaceListGetResponse.fromEntity(invitatedWorkPlaceGetResponseList, connectedWorkPlaceGetResponseList, customWorkPlaceGetResponseList);
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
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour()))
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
                    .mapToInt(customAttendanceMemo -> calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getRestMinute(), customAttendanceMemo.getPayPerHour()))
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

        LocalDate yesterday = LocalDate.now().minusDays(1);
        String yesterDate = String.format("%04d%02d%02d", yesterday.getYear(), yesterday.getMonthValue(), yesterday.getDayOfMonth());
        Integer connectedCurrentPayment = 0;
        Integer connectedTotalPayment = 0;
        Integer notConnectedCurrentPayment = 0;
        Integer notConnectedTotalPayment = 0;

        // 연결근무지 : attendance 모두 찾기
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(
                    workPlaceEmployee, searchDate
            );

            for (Attendance attendance : attendanceList) {
                AttendanceType attendanceType = attendance.getAttendanceType();
                int payment = calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour());
                if (attendance.getAttendDate().compareTo(yesterDate) <= 0) {    // 1일~어제 까지
                    connectedCurrentPayment += payment;
                }
                connectedTotalPayment += payment;

                employeeSalaryCalendarGetResponseList.add(
                        new EmployeeSalaryCalendarGetResponse(
                                true,
                                workPlaceEmployee.getWorkPlace().getWorkPlaceId(),
                                workPlaceEmployee.getWorkPlace().getWorkPlaceNm(),
                                workPlaceEmployee.getColorType().getCode(),
                                attendance.getAttendDate(),
                                attendanceType,
                                attendanceType == AttendanceType.REAL ? attendance.getRealStartTime() : attendance.getStartTime(),
                                attendanceType == AttendanceType.REAL ? attendance.getRealEndTime() : attendance.getEndTime(),
                                attendance.getRestMinute(),
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

            for (CustomAttendanceMemo customAttendanceMemo : customAttendanceMemoList) {
                int payment = calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getRestMinute(), customAttendanceMemo.getPayPerHour());
                if (customAttendanceMemo.getAttendDate().compareTo(yesterDate) <= 0) {    // 1일~어제 까지
                    notConnectedCurrentPayment += payment;
                }
                notConnectedTotalPayment += payment;

                employeeSalaryCalendarGetResponseList.add(
                        new EmployeeSalaryCalendarGetResponse(
                                false,
                                customWorkPlace.getCustomWorkPlaceId(),
                                customWorkPlace.getCustomWorkPlaceNm(),
                                customWorkPlace.getColorType().getCode(),
                                customAttendanceMemo.getAttendDate(),
                                AttendanceType.EXPECT,
                                customAttendanceMemo.getStartTime(),
                                customAttendanceMemo.getEndTime(),
                                customAttendanceMemo.getRestMinute(),
                                payment
                        )
                );
            }
        }

        return EmployeeSalaryCalendarListGetResponse.fromEntity(
                connectedCurrentPayment + notConnectedCurrentPayment,
                connectedTotalPayment + notConnectedTotalPayment,
                connectedCurrentPayment,
                connectedTotalPayment,
                employeeSalaryCalendarGetResponseList);
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
