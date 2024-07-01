package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.EmployeeAndWorkPlaceEmployeeConnectResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractListGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.MonthlyPayStubGetResponse;
import com.project.hana_on_and_on_channel_server.paper.exception.EmployeeContractAlreadyConnectedException;
import com.project.hana_on_and_on_channel_server.paper.exception.EmploymentContractNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.exception.PayStubNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.PayStubRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
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
                    "REAL",
                    searchMonth
            );

            //기본 시간, 연장 시간
            int totalBasicHours = 0;
            int totalOverHours = 0;

            for (Attendance attendance : attendanceList) {
                Duration duration = Duration.between(attendance.getRealStartTime(), attendance.getRealEndTime());

                long minutes = duration.toMinutes();
                double hours = minutes / 60.0;

                int basicHour = (int) Math.min(hours, 8);
                int overHour = (int) Math.max(hours - 8, 0);

                totalBasicHours += basicHour;
                totalOverHours += overHour;
            }

            //주휴 시간 => 총 기본 시간 /4가 15넘으면 기본 시간*4
            // TODO 주휴 계산 정확하게
            int totalWeeklyHolidayHours = totalBasicHours / 4 >= 15 ? 7 : 0;

            double totalPay = totalBasicHours * payPerHour + totalOverHours * payPerHour * 1.5 + totalWeeklyHolidayHours * payPerHour;
            double totalTaxPay = Math.floor(totalPay * 0.094);

            return MonthlyPayStubGetResponse.builder()
                    .payStubId(null)
                    .workPlaceEmployeeId(workPlaceEmployeeId).year(year).month(month)
                    .status(null)
                    .salary((long) (totalPay - totalTaxPay))
                    .totalPay((long) totalPay).totalTaxPay((long) totalTaxPay)
                    .paymentDay(employmentContract.getPaymentDay()).payPerHour(payPerHour)
                    .basicHour((long) totalBasicHours).basicPay(totalBasicHours * payPerHour)
                    .overHour((long) totalOverHours).overPay((long) (totalOverHours * payPerHour * 1.5))
                    .weeklyHolidayTime((long) totalWeeklyHolidayHours).weeklyHolidayPay(totalWeeklyHolidayHours * payPerHour)
                    .taxRate(0.094).taxPay((long) totalTaxPay)
                    .build();
        } else {
            // 당월 X - 급여명세서 조회
            PayStub payStub = payStubRepository.findByWorkPlaceEmployeeIdAndYearAndMonth(workPlaceEmployeeId, year, month)
                    .orElseThrow(PayStubNotFoundException::new);

            return MonthlyPayStubGetResponse.builder()
                    .payStubId(payStub.getPayStubId())
                    .workPlaceEmployeeId(workPlaceEmployeeId).year(year).month(month)
                    .status("READY")
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
}
