package com.project.hana_on_and_on_channel_server.employee.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceInvalidException;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeDuplicatedException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomAttendanceMemoRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeInvalidException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.NotificationRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.PayStubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;
import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDFormat;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractRepository employmentContractRepository;
    private final CustomWorkPlaceRepository customWorkPlaceRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final CustomAttendanceMemoRepository customAttendanceMemoRepository;
    private final PayStubRepository payStubRepository;
    private final NotificationRepository notificationRepository;
    private final WorkPlaceRepository workPlaceRepository;

    @Transactional(readOnly = true)
    public EmployeeAccountGetResponse getEmployeeAccount(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        return EmployeeAccountGetResponse.fromEntity(employee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAccountUpsertResponse registerEmployeeAccount(Long userId, EmployeeAccountRegRequest dto){
        // employee 존재 여부 확인
        employeeRepository.findByUserId(userId).ifPresent(employee -> {
            throw new EmployeeDuplicatedException();
        });

        // employee 등록
        Employee employee = employeeRepository.save(Employee.builder()
                .userId(userId)
                .accountNumber(dto.accountNumber())
                .employeeNm(dto.employeeNm())
                .build());
        return EmployeeAccountUpsertResponse.fromEntity(employee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmployeeAccount(Long userId, EmployeeAccountUpsertRequest dto){
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);
        employee.registerAccountNumber(dto.accountNumber());
    }

    @Transactional(readOnly = true)
    public EmployeeWorkPlaceListGetResponse getWorkPlaces(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // 1. 초대된 사업장 : 휴대폰 번호로, 전자서명 안 된 계약서 찾기
        List<EmploymentContract> employmentContractList = employmentContractRepository.findByEmployeePhoneAndEmployeeSign(employee.getPhoneNumber(), false);
        List<EmployeeWorkPlaceGetResponse> invitatedWorkPlaceGetResponseList = employmentContractList.stream()
                .map(employmentContract -> EmployeeWorkPlaceGetResponse.fromEntity(employmentContract))
                .toList();

        // 2. 연결된 사업장
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);
        List<EmployeeWorkPlaceGetResponse> connectedWorkPlaceGetResponseList = workPlaceEmployeeList.stream()
                .map(workPlaceEmployee -> EmployeeWorkPlaceGetResponse.fromEntity(workPlaceEmployee))
                .toList();

        // 3. 수동 사업장
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);
        List<EmployeeWorkPlaceGetResponse> customWorkPlaceGetResponseList = customWorkPlaceList.stream()
                .map(customWorkPlace -> EmployeeWorkPlaceGetResponse.fromEntity(customWorkPlace))
                .toList();

        return new EmployeeWorkPlaceListGetResponse(invitatedWorkPlaceGetResponseList, connectedWorkPlaceGetResponseList, customWorkPlaceGetResponseList);
    }

    @Transactional(readOnly = true)
    public EmployeeWorkPlaceCustomListGetResponse getCustomWorkPlaces(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // 수동 사업장
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);
        List<EmployeeWorkPlaceGetResponse> customWorkPlaceGetResponseList = customWorkPlaceList.stream()
                .map(customWorkPlace -> EmployeeWorkPlaceGetResponse.fromEntity(customWorkPlace))
                .toList();

        return new EmployeeWorkPlaceCustomListGetResponse(customWorkPlaceGetResponseList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeWorkPlaceCustomCreateResponse createCustomWorkPlaces(Long userId, EmployeeWorkPlaceCustomCreateRequest dto) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // customWorkPlace 등록
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.save(CustomWorkPlace.builder()
                        .employee(employee)
                        .customWorkPlaceNm(dto.customWorkPlaceNm())
                        .payPerHour(dto.payPerHour())
                .build());
        return EmployeeWorkPlaceCustomCreateResponse.fromEntity(customWorkPlace);
    }

    @Transactional(readOnly = true)
    public EmployeeNotificationRecentGetResponse getRecentNotification(Long userId, Long workPlaceId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // employee가 근무하는 workPlace가 맞는지 검증
        workPlaceEmployeeRepository.findByWorkPlaceWorkPlaceIdAndEmployeeEmployeeId(workPlaceId, employee.getEmployeeId())
                .orElseThrow(WorkPlaceEmployeeInvalidException::new);

        Notification notification = notificationRepository.findTop1ByWorkPlaceWorkPlaceIdOrderByCreatedAtDesc(workPlaceId)
                .orElse(null);

        return EmployeeNotificationRecentGetResponse.fromEntity(notification);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAttendanceCustomCreateResponse saveCustomAttendanceMemo(Long userId, EmployeeAttendanceCustomCreateRequest dto){
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.findById(dto.customWorkPlaceId())
                .orElseThrow(CustomWorkPlaceNotFoundException::new);

        // employee가 근무하는 customWorkPlace가 맞는지 검증
        if (employee != customWorkPlace.getEmployee()) {
            throw new CustomWorkPlaceInvalidException();
        }

        // CustomAttendanceMemo 생성
        CustomAttendanceMemo customAttendanceMemo = customAttendanceMemoRepository.save(CustomAttendanceMemo.builder()
                .customWorkPlace(customWorkPlace)
                .payPerHour(dto.payPerHour())
                .attendDate(localDateTimeToYMDFormat(dto.startTime()))
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .restMinute(dto.restMinute())
                .build()
        );

        return EmployeeAttendanceCustomCreateResponse.fromEntity(customAttendanceMemo);
    }

    @Transactional(readOnly = true)
    public EmployeeSalaryListGetResponse getSalaries(Long userId, Integer year, Integer month) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // year + month => yyyymm
        String searchDate = String.format("%04d%02d", year, month);

        // attendance + CustomAttendanceMemo
        List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList = new ArrayList<>();

        // 1. 연결근무지 : attendance 모두 찾기
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployee(employee);

        for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendDateStartingWith(workPlaceEmployee, searchDate);

            int payment = attendanceList.stream()
                    .mapToInt(attendance -> calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour()))
                    .sum();

            PayStub payStub = payStubRepository.findByWorkPlaceEmployeeIdAndYearAndMonth(workPlaceEmployee.getWorkPlaceEmployeeId(), year, month)
                            .orElse(null);

            employeeSalaryGetResponseList.add(
                    EmployeeSalaryGetResponse.fromEntity(workPlaceEmployee, payStub, payment)
            );
        }

        // 2. 수동근무지 : CustomAttendanceMemo 모두 찾기
        List<CustomWorkPlace> customWorkPlaceList = customWorkPlaceRepository.findByEmployee(employee);

        for (CustomWorkPlace customWorkPlace : customWorkPlaceList) {
            List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndAndAttendDateStartingWith(customWorkPlace, searchDate);

            int payment = customAttendanceMemoList.stream()
                    .mapToInt(customAttendanceMemo -> calculateDailyPayment(customAttendanceMemo.getStartTime(), customAttendanceMemo.getEndTime(), customAttendanceMemo.getRestMinute(), customAttendanceMemo.getPayPerHour()))
                    .sum();

            employeeSalaryGetResponseList.add(
                    EmployeeSalaryGetResponse.fromEntity(customWorkPlace, payment)
            );
        }

        // totalPayment 계산
        Integer totalPayment = employeeSalaryGetResponseList.stream()
                .mapToInt(EmployeeSalaryGetResponse::payment)
                .sum();

        return new EmployeeSalaryListGetResponse(year, month, totalPayment, employeeSalaryGetResponseList);
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
                int payment = calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour());
                if (attendance.getAttendDate().compareTo(yesterDate) <= 0) {    // 1일~어제 까지
                    connectedCurrentPayment += payment;
                }
                connectedTotalPayment += payment;

                employeeSalaryCalendarGetResponseList.add(
                        EmployeeSalaryCalendarGetResponse.fromEntity(workPlaceEmployee, attendance, payment)
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
                        EmployeeSalaryCalendarGetResponse.fromEntity(customWorkPlace, customAttendanceMemo, payment)
                );
            }
        }

        // attendDate 순 정렬
        Collections.sort(employeeSalaryCalendarGetResponseList, Comparator.comparing(EmployeeSalaryCalendarGetResponse::attendDate));

        return new EmployeeSalaryCalendarListGetResponse(
                connectedCurrentPayment + notConnectedCurrentPayment,
                connectedTotalPayment + notConnectedTotalPayment,
                connectedCurrentPayment,
                connectedTotalPayment,
                employeeSalaryCalendarGetResponseList);
    }
}
