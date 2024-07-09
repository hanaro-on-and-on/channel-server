package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.account.service.AccountService;
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
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.exception.*;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.*;
import com.project.hana_on_and_on_channel_server.paper.dto.PaperWorkPlaceGetResponse;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.enumType.PayStubStatus;
import com.project.hana_on_and_on_channel_server.paper.dto.*;
import com.project.hana_on_and_on_channel_server.paper.exception.*;
import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.PayStubRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.SalaryTransferReserveRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDFormat;

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
    private final SalaryTransferReserveRepository salaryTransferReserveRepository;

    private final AccountService accountService;
    private final OwnerRepository ownerRepository;

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
                .workEndDate(request.workEndDate())
                .workSite(request.workSite())
                .workDetail(request.workDetail())
                .payPerHour(request.payPerHour())
                .paymentDay(request.paymentDay())
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

        // 예상 근무 생성
        createExpectedAttendance(employmentContract, LocalDate.now(), employmentContract.getWorkEndDate());

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


    @Transactional(readOnly = true)
    public PaperPayStubWorkPlaceEmployeeGetResponse getPayStubWorkPlaceEmployeeInfo(Long userId, Long workPlaceEmployeeId) {
        // TODO : owner, employee 모두 가능
//        // owner 존재 여부 확인
//        Owner owner = ownerRepository.findByUserId(userId).orElseThrow(OwnerNotFoundException::new);

        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(workPlaceEmployeeId).orElseThrow(WorkPlaceEmployeeNotFoundException::new);

//        // owner가 소유한 workPlace가 맞는지 검증
//        if (owner != workPlaceEmployee.getWorkPlace().getOwner()) {
//            throw new WorkPlaceEmployeeInvalidException();
//        }

        return PaperPayStubWorkPlaceEmployeeGetResponse.fromEntity(workPlaceEmployee);
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

    @Transactional(propagation = Propagation.REQUIRED)
    public SalaryTransferReserveResponse reservePayStub(Long userId, Long payStubId, SalaryTransferReserveRequest request){
        PayStub payStub = payStubRepository.findById(payStubId).orElseThrow(PayStubNotFoundException::new);

        // 사장님이 아닐 경우 예외 처리
        if(userId != payStub.getWorkPlaceEmployee().getWorkPlace().getOwner().getUserId()){
            throw new PayStubInvalidException(payStubId);
        }

        // 지급 상태 READY가 아닐 경우 예외 처리
        if(payStub.getStatus()!=PayStubStatus.READY){
            throw new PayStubInvalidException(payStubId);
        }

        // 근로계약서에서 월급날 가져오기
        EmploymentContract employmentContract = employmentContractRepository.findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(payStub.getWorkPlaceEmployee()).orElseThrow(EmployeeNotFoundException::new);
        LocalDateTime reserveDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), Math.toIntExact(employmentContract.getPaymentDay())).atStartOfDay();
        SalaryTransferReserve savedSalaryTransferReserve = salaryTransferReserveRepository.save(
                SalaryTransferReserve.builder()
                        .payStub(payStub)
                        .totalPay(payStub.calcTotalPay()-payStub.calcTotalTaxPay(0.094))
                        .reserveDate(localDateTimeToYMDFormat(reserveDate))
                        .senderNm(request.senderNm())
                        .senderAccountNumber(payStub.getWorkPlaceEmployee().getWorkPlace().getOwner().getAccountNumber())
                        .receiverNm(request.receiverNm())
                        .receiverAccountNumber(payStub.getWorkPlaceEmployee().getEmployee().getAccountNumber())
                        .build()
        );

        payStub.reserveTransfer();

        return SalaryTransferReserveResponse.fromEntity(savedSalaryTransferReserve);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void executeTodayReservedTransfer(){
        String todayDate = String.format("%d%02d%02d", LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        // 이체일: 오늘 기준 이전 날짜, 아직 이체되지 않은 예약 정보
        List<SalaryTransferReserve> salaryTransferReserveList = salaryTransferReserveRepository.findPendingTransfersBeforeOrEqualDate(todayDate);
        salaryTransferReserveList.forEach(this::processReservedTransfer);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void processReservedTransfer(SalaryTransferReserve salaryTransferReserve){
        PayStub payStub = salaryTransferReserve.getPayStub();

        //급여명세서에 직원 서명 안되어있거나 지급상태가 WAITING이 아닐 경우 이체 진행 X
        if(payStub.getEmployeeSign() == Boolean.FALSE || payStub.getStatus() != PayStubStatus.WAITING){
            return;
        }

        ResponseEntity<Void> response = accountService.sendAccountDebitRequest(salaryTransferReserve);

        // 이체 성공 시 상태 업데이트
        if (response.getStatusCode().is2xxSuccessful()) {
            salaryTransferReserve.completeTransfer();
            payStub.completeTransfer();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void executeMonthlyPayStubGeneration(){
        List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByEmployeeStatus(EmployeeStatus.WORKING);
        workPlaceEmployeeList.forEach(this::createPayStub);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void createPayStub(WorkPlaceEmployee workPlaceEmployee){
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

    private void createExpectedAttendance(EmploymentContract employmentContract, LocalDate workStartDate, LocalDate workEndDate){
        List<WorkTime> workTimes = workTimeRepository.findByEmploymentContract(employmentContract);
        List<Attendance> attendances = new ArrayList<>();

        for (LocalDate date = workStartDate; !date.isAfter(workEndDate); date = date.plusDays(1)) {
            String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE", Locale.KOREAN));
            for (WorkTime workTime : workTimes) {
                if (workTime.getWorkDayOfWeek().equals(dayOfWeek)) {
                    LocalDateTime startTime = LocalDateTime.of(date, workTime.getWorkStartTime().toLocalTime());
                    LocalDateTime endTime = LocalDateTime.of(date, workTime.getWorkEndTime().toLocalTime());

                    Attendance attendance = Attendance.builder()
                            .workPlaceEmployee(employmentContract.getWorkPlaceEmployee())
                            .attendanceType(AttendanceType.EXPECT)
                            .payPerHour(employmentContract.getPayPerHour())
                            .attendDate(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .startTime(startTime)
                            .endTime(endTime)
                            .restMinute(calcMinutes(workTime.getRestStartTime(), workTime.getRestEndTime()))
                            .build();

                    attendances.add(attendance);
                }
            }
        }
        attendanceRepository.saveAll(attendances);
    }

    //TODO 계산 로직 util로 빼기
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

    public static int calcMinutes(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        return (int) duration.toMinutes();
    }
}
