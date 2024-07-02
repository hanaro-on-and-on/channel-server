package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomAttendanceMemoRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import com.project.hana_on_and_on_channel_server.paper.domain.TotalHours;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.domain.enumType.PayStubStatus;
import com.project.hana_on_and_on_channel_server.paper.dto.*;
import com.project.hana_on_and_on_channel_server.paper.exception.EmployeeContractAlreadyConnectedException;
import com.project.hana_on_and_on_channel_server.paper.exception.EmploymentContractNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.exception.PayStubInvalidException;
import com.project.hana_on_and_on_channel_server.paper.exception.PayStubNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.PayStubRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final EmploymentContractRepository employmentContractRepository;
    private final WorkTimeRepository workTimeRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayStubRepository payStubRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final CustomWorkPlaceRepository customWorkPlaceRepository;
    private final CustomAttendanceMemoRepository customAttendanceMemoRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmploymentContractListGetResponse> findEmploymentContractList (Long userId){
        List<EmploymentContractSummary> employmentContractList = employmentContractRepository.findEmploymentContractList(userId);

        return employmentContractList.stream().map(EmploymentContractListGetResponse::fromProjection).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmploymentContractGetResponse findEmploymentContract(Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);
        List<WorkTime> workTimeList = workTimeRepository.findByEmploymentContractEmploymentContractId(employmentContractId);
        String workPlaceName = employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceNm();

        return EmploymentContractGetResponse.fromEntity(employmentContract, workTimeList, workPlaceName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAndWorkPlaceEmployeeConnectResponse signEmploymentContractAndConnectEmployeeToWorkPlace(Long userId, Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);

        // 근로계약서 서명되어있을 경우 예외처리
        if(employmentContract.getEmployeeSign()){
            throw new EmployeeContractAlreadyConnectedException();
        }

        // 근로계약서 서명
        employmentContract.registerEmployeeSign(Boolean.TRUE);

        // 직원 연동
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = employmentContract.getWorkPlaceEmployee();
        workPlaceEmployee.registerEmployee(employee);

        return new EmployeeAndWorkPlaceEmployeeConnectResponse(workPlaceEmployee.getWorkPlaceEmployeeId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MonthlyPayStubGetResponse getMonthlyPayStubWithAttendance(Long userId, Long workPlaceEmployeeId, int year, int month){
        //근로계약서 가져오기
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(workPlaceEmployeeId).orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        EmploymentContract employmentContract = employmentContractRepository.findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(workPlaceEmployee)
                .orElseThrow(EmploymentContractNotFoundException::new);
        Long payPerHour = employmentContract.getPayPerHour();

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        if (year == currentYear && month == currentMonth) {
            //당월 - 계산만
            //attendance 조회
            String searchMonth = String.format("%d%02d", year, month);
            List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
                    workPlaceEmployee,
                    AttendanceType.REAL,
                    searchMonth
            );

            //기본 시간, 연장 시간, 주휴시간 계산
            TotalHours totalHours = calcTotalHours(attendanceList);

            Long totalPay = totalHours.calcTotalPay(payPerHour);
            Long totalTaxPay = (long)Math.floor(totalPay * 0.094);

            return MonthlyPayStubGetResponse.builder()
                    .payStubId(null)
                    .workPlaceEmployeeId(workPlaceEmployeeId)
                    .year(year).month(month)
                    .status(null)
                    .salary(totalPay - totalTaxPay)
                    .totalPay(totalPay).totalTaxPay(totalTaxPay)
                    .paymentDay(employmentContract.getPaymentDay()).payPerHour(payPerHour)
                    .basicHour(totalHours.totalBasicHours()).basicPay(totalHours.totalBasicHours() * payPerHour)
                    .overHour(totalHours.totalOverHours()).overPay((long)(totalHours.totalOverHours() * payPerHour * 1.5))
                    .weeklyHolidayTime(totalHours.totalWeeklyHolidayHours()).weeklyHolidayPay(totalHours.totalWeeklyHolidayHours() * payPerHour)
                    .taxRate(0.094).taxPay(totalTaxPay)
                    .build();
        } else {
            // 당월 X - 급여명세서 조회
            PayStub payStub = payStubRepository.findByWorkPlaceEmployeeIdAndYearAndMonth(workPlaceEmployeeId, year, month)
                    .orElseThrow(PayStubNotFoundException::new);

            return MonthlyPayStubGetResponse.builder()
                    .payStubId(payStub.getPayStubId())
                    .workPlaceEmployeeId(workPlaceEmployeeId)
                    .year(year).month(month)
                    .status(payStub.getStatus().toString())
                    .salary(payStub.calcTotalPay()-payStub.calcTotalTaxPay(0.094))
                    .totalPay(payStub.calcTotalPay()).totalTaxPay(payStub.calcTotalTaxPay(0.094))
                    .paymentDay(employmentContract.getPaymentDay()).payPerHour(payStub.getPayPerHour())
                    .basicHour(payStub.getBasicHour()).basicPay(payStub.calcBasicPay())
                    .overHour(payStub.getOverHour()).overPay(payStub.calcOverPay(1.5))
                    .weeklyHolidayTime(payStub.getWeeklyHolidayTime()).weeklyHolidayPay(payStub.calcWeeklyHolidayPay())
                    .taxRate(9.4).taxPay(payStub.calcTotalTaxPay(0.094))
                    .build();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MonthlyPayStubGetResponse getMonthlyPayStubWithCustomAttendance(Long userId, Long customWorkPlaceId, int year, int month){
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.findById(customWorkPlaceId).orElseThrow(CustomWorkPlaceNotFoundException::new);

        String searchDate = String.format("%d%02d", year, month);
        List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndAndAttendDateStartingWith(customWorkPlace, searchDate);

        long basicHour = customAttendanceMemoList.stream()
                .mapToInt(customAttendaceMemo -> calculateDailyBasicHour(customAttendaceMemo.getStartTime(), customAttendaceMemo.getEndTime()))
                .sum();

        return MonthlyPayStubGetResponse.builder()
                .payStubId(null)
                .workPlaceEmployeeId(null).year(year).month(month)
                .status(null)
                .salary(basicHour*customWorkPlace.getPayPerHour()-(long)Math.floor(basicHour*customWorkPlace.getPayPerHour()*0.094))
                .totalPay(basicHour*customWorkPlace.getPayPerHour())
                .totalTaxPay((long)Math.floor(basicHour*customWorkPlace.getPayPerHour()*0.094))
                .paymentDay(null).payPerHour(customWorkPlace.getPayPerHour())
                .basicHour(basicHour)
                .basicPay(basicHour*customWorkPlace.getPayPerHour())
                .overHour(null).overPay(null).weeklyHolidayTime(null).weeklyHolidayPay(null)
                .taxRate(9.4).taxPay((long)Math.floor(basicHour*customWorkPlace.getPayPerHour()*0.094))
                .build();
    }

    private Integer calculateDailyBasicHour(LocalDateTime startTime, LocalDateTime endTime) {

        LocalDateTime startZeroSecondTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), startTime.getHour(), startTime.getMinute(), 0);
        LocalDateTime endZeroSecondTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), endTime.getHour(), endTime.getMinute(), 0);
        int totalDurationMinutes = (int) Duration.between(startZeroSecondTime, endZeroSecondTime).toMinutes();

        double basicHour = totalDurationMinutes / 60.0;
        return (int)Math.floor(basicHour);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PayStubSignResponse signPayStub(Long userId, Long payStubId){
        PayStub payStub = payStubRepository.findById(payStubId).orElseThrow(PayStubNotFoundException::new);

        // 본인 급여명세서가 아닐 경우 예외 처리
        if(userId != payStub.getWorkPlaceEmployee().getEmployee().getUserId()){
            throw new PayStubInvalidException(payStubId);
        }

        // 서명요청 상태가 아닐 경우 예외 처리
        if(payStub.getStatus()!= PayStubStatus.SIGN){
            throw new PayStubInvalidException(payStubId);
        }

        // payStub 상태를 WATING, employee 서명을 true로 변경
        payStub.registerSign();

        return new PayStubSignResponse(payStubId);
    }

    private void createPayStub(WorkPlaceEmployee workPlaceEmployee){
        // TODO 스케줄러 작성 (매달 1일마다 실행되도록)
        
        EmploymentContract employmentContract = employmentContractRepository.findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(workPlaceEmployee)
                .orElseThrow(EmploymentContractNotFoundException::new);

        // 직전 달
        LocalDate now = LocalDate.now().minusMonths(1);
        String searchMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));

        List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
                workPlaceEmployee,
                AttendanceType.REAL,
                searchMonth
        );

        TotalHours totalHours = calcTotalHours(attendanceList);

        PayStub payStub = PayStub.builder()
                .workPlaceEmployee(workPlaceEmployee)
                .payPerHour(employmentContract.getPayPerHour())
                .basicHour(totalHours.totalBasicHours())
                .overHour(totalHours.totalOverHours())
                .weeklyHolidayTime(totalHours.totalWeeklyHolidayHours())
                .tax(BigDecimal.valueOf(0.094))
                .build();

        payStubRepository.save(payStub);
    }

    private TotalHours calcTotalHours(List<Attendance> attendanceList){
        long totalBasicHours = 0;
        long totalOverHours = 0;

        for (Attendance attendance : attendanceList) {
            Duration duration = Duration.between(attendance.getRealStartTime(), attendance.getRealEndTime());

            long minutes = duration.toMinutes();
            double hours = minutes / 60.0;

            int basicHour = (int) Math.min(hours, 8);
            int overHour = (int) Math.max(hours - 8, 0);

            totalBasicHours += basicHour;
            totalOverHours += overHour;
        }

        long totalWeeklyHolidayHours = totalBasicHours / 4 >= 15 ? 7 : 0;

        return new TotalHours(totalBasicHours, totalOverHours, totalWeeklyHolidayHours);
    }
}
