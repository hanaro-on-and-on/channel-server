package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name="employment_contracts")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class EmploymentContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employmentContractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_place_employee_id")
    private WorkPlaceEmployee workPlaceEmployee; // 알바생_사업장

    @Column(name = "work_start_date")
    private LocalDate workStartDate;

    @Column(name = "work_end_date")
    private LocalDate workEndDate;

    @Column(name = "work_site")
    private String workSite;

    @Column(name = "work_detail")
    private String workDetail;

    @Column(name = "pay_per_hour")
    private Long payPerHour;

    @Column(name = "payment_day")
    private Long paymentDay;

    @Column(name = "employee_nm")
    private String employeeNm;

    @Column(name = "employee_address")
    private String employeeAddress;

    @Column(name = "employee_phone")
    private String employeePhone;

    @Column(name = "rest_day_of_week")
    private String restDayOfWeek;

    @Column(name = "bonus_amount")
    private Long bonusAmount;

    @Column(name = "other_allowances_amount")
    private Long otherAllowancesAmount;

    @Column(name = "other_allowances_name")
    private String otherAllowancesName;

    @Column(name = "overtime_rate")
    private Long overtimeRate;

    @Column(name = "owner_sign")
    private Boolean ownerSign;

    @Column(name = "employee_sign")
    private Boolean employeeSign;

    public void registerEmployeeSign(Boolean employeeSign){
        this.employeeSign=employeeSign;
    }

    @Builder
    public EmploymentContract(WorkPlaceEmployee workPlaceEmployee, LocalDate workStartDate, LocalDate workEndDate, String workSite,
                              String workDetail, Long payPerHour, Long paymentDay, String employeeNm, String employeeAddress,
                              String employeePhone, String restDayOfWeek, Long otherAllowancesAmount, Long bonusAmount,
                              String otherAllowancesName, Long overtimeRate, Boolean ownerSign, Boolean employeeSign) {
        this.workPlaceEmployee = workPlaceEmployee;
        this.workStartDate = workStartDate;
        this.workEndDate = workEndDate;
        this.workSite = workSite;
        this.workDetail = workDetail;
        this.payPerHour = payPerHour;
        this.paymentDay = paymentDay;
        this.employeeNm = employeeNm;
        this.employeeAddress = employeeAddress;
        this.employeePhone = employeePhone;
        this.restDayOfWeek = restDayOfWeek;
        this.otherAllowancesAmount = otherAllowancesAmount;
        this.bonusAmount = bonusAmount;
        this.otherAllowancesName = otherAllowancesName;
        this.overtimeRate = overtimeRate;
        this.ownerSign = ownerSign;
        this.employeeSign = employeeSign;
    }
}
