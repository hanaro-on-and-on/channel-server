package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceInvalidException;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeInvalidException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomAttendanceMemoRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerInvalidException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeInvalidException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.paper.dto.PaperWorkPlaceGetResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import com.project.hana_on_and_on_channel_server.paper.domain.TotalHours;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.domain.enumType.PayStubStatus;
import com.project.hana_on_and_on_channel_server.paper.dto.*;
import com.project.hana_on_and_on_channel_server.paper.exception.*;
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
    private final WorkPlaceRepository workPlaceRepository;

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
    public PaperWorkPlaceGetResponse findWorkPlace(Long userId, Long workPlaceEmployeeId){
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(workPlaceEmployeeId)
                .orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        EmploymentContract employmentContract = employmentContractRepository.findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(workPlaceEmployee)
                .orElseThrow(EmploymentContractNotFoundException::new);

        //유저 검증
        if(userId != workPlaceEmployee.getEmployee().getUserId()){
            throw new EmployeeInvalidException(workPlaceEmployee.getEmployee().getUserId());
        }
        return new PaperWorkPlaceGetResponse(workPlaceEmployeeId, workPlaceEmployee.getWorkPlace().getWorkPlaceNm(), workPlaceEmployee.getWorkPlace().getColorType().getCode(), employmentContract.getWorkStartDate().toString());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaperCustomWorkPlaceGetResponse findCustomWorkPlace(Long userId, Long customWorkPlaceId){
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.findById(customWorkPlaceId)
                .orElseThrow(CustomWorkPlaceNotFoundException::new);

        //유저 검증
        if(userId != customWorkPlace.getEmployee().getUserId()){
            throw new EmployeeInvalidException(customWorkPlace.getEmployee().getUserId());
        }
        return new PaperCustomWorkPlaceGetResponse(customWorkPlace.getCustomWorkPlaceNm(), customWorkPlace.getColorType().getCode());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmploymentContractUpsertResponse saveEmploymentContract(Long userId, Long workPlaceId, EmploymentContractUpsertRequest request){
        WorkPlace workPlace = workPlaceRepository.findById(workPlaceId)
                .orElseThrow(WorkPlaceNotFoundException::new);

        //해당 사업장의 owner가 아닐 경우 예외처리
        if(userId != workPlace.getOwner().getUserId()){
            throw new OwnerInvalidException(userId);
        }

        // workPlaceEmployee 생성
        WorkPlaceEmployee workPlaceEmployee = WorkPlaceEmployee.builder()
                .workPlace(workPlace)
                .employeeStatus(EmployeeStatus.WORKING)
                .workStartDate(request.workStartDate())
                .colorType(workPlace.getColorType())
                .build();

        // 근로계약서 생성
        EmploymentContract employmentContract = EmploymentContract.builder()
                .workPlaceEmployee(workPlaceEmployee)
                .workStartDate(request.workStartDate()) //TODO LocalDateTime LocalTime 등 맞추기
                .workSite(request.workSite())
                .workDetail(request.workDetail())
                .payPerHour(request.payPerHour())
                .employeeNm(request.employeeNm())
                .employeeAddress(request.employeeAddress())
                .employeePhone(request.employeePhone())
                .restDayOfWeek(request.restDayOfWeek())
                .otherAllowancesAmount(request.otherAllowancesAmount())
                .bonusAmount(request.bonusAmount())
                .otherAllowancesName(request.otherAllowancesName())
                .overtimeRate(request.overTimeRate())
                .ownerSign(Boolean.TRUE)
                .employeeSign(Boolean.FALSE)
                .build();

        workPlaceEmployeeRepository.save(workPlaceEmployee);
        employmentContractRepository.save(employmentContract);

        //근무요일 추가
        LocalDate today = LocalDate.now();
        request.workTimes().stream()
                .map(workTimeRequest -> WorkTime.builder()
                        .employmentContract(employmentContract)
                        .workDayOfWeek(workTimeRequest.workDayOfWeek())
                        .workStartTime(LocalDateTime.of(today, workTimeRequest.workStartTime()))
                        .workEndTime(LocalDateTime.of(today, workTimeRequest.workEndTime()))
                        .restStartTime(LocalDateTime.of(today, workTimeRequest.restStartTime()))
                        .restEndTime(LocalDateTime.of(today, workTimeRequest.restEndTime()))
                        .build())
                .forEach(workTime -> workTimeRepository.save(workTime));

        return new EmploymentContractUpsertResponse(employmentContract.getEmploymentContractId(), workPlaceEmployee.getWorkPlaceEmployeeId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAndWorkPlaceEmployeeConnectResponse signEmploymentContractAndConnectEmployeeToWorkPlace(Long userId, Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);

        // 본인 근로계약서가 아닐 경우 예외처리
        if(userId != employmentContract.getWorkPlaceEmployee().getEmployee().getUserId()){
            throw new EmploymentContractInvalidException(employmentContractId);
        }

        // 근로계약서 서명되어있을 경우 예외처리
        if(employmentContract.getEmployeeSign()){
            throw new EmploymentContractAlreadyConnectedException();
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
    public MonthlyAttendanceGetResponse getMonthlyAttendance(Long userId, Long workPlaceEmployeeId, int year, int month){
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(workPlaceEmployeeId)
                .orElseThrow(WorkPlaceNotFoundException::new);
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();

        // workPlaceEmployee와 관련된 사용자(알바생 본인, 사장님)가 아닐 경우 예외 처리
        if(userId != workPlaceEmployee.getEmployee().getUserId() || userId != workPlace.getOwner().getUserId()){
            throw new WorkPlaceEmployeeInvalidException(workPlaceEmployeeId);
        }

        String searchMonth = String.format("%d%02d", year, month);

        List<Attendance> attendanceList = attendanceRepository.findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
                workPlaceEmployee,
                AttendanceType.REAL,
                searchMonth
        );

        return MonthlyAttendanceGetResponse.fromAttendance(workPlace, attendanceList, year, month);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MonthlyAttendanceGetResponse getMonthlyCustomAttendance(Long userId, Long customWorkPlaceId, int year, int month){
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.findById(customWorkPlaceId)
                .orElseThrow(CustomWorkPlaceNotFoundException::new);

        if(userId != customWorkPlace.getEmployee().getUserId()){
            throw new CustomWorkPlaceInvalidException(customWorkPlaceId);
        }

        String searchDate = String.format("%d%02d", year, month);
        List<CustomAttendanceMemo> customAttendanceMemoList = customAttendanceMemoRepository.findByCustomWorkPlaceAndAndAttendDateStartingWith(customWorkPlace, searchDate);

        return MonthlyAttendanceGetResponse.fromCustomAttendance(customWorkPlace, customAttendanceMemoList, year, month);
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
