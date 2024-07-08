package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.paper.domain.enumType.PayStubStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="pay_stubs")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class PayStub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payStubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_place_employee_id")
    private WorkPlaceEmployee workPlaceEmployee;

    @Column(name = "status_type_cd")
    @Enumerated(EnumType.STRING)
    private PayStubStatus status;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(nullable = false)
    private Boolean employeeSign;

    @Column(nullable = false)
    private Long weeklyHolidayTime;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal tax;

    public Long calcWeeklyHolidayPay(){
        return this.weeklyHolidayTime * this.payPerHour;
    }

    public void reserveTransfer(){
        this.status = PayStubStatus.SIGN;
    }

    public void registerSign(){
        this.status = PayStubStatus.WAITING;
        this.employeeSign = Boolean.TRUE;
    }

    public void completeTransfer(){
        this.status = PayStubStatus.COMPLETED;
    }

    @Builder
    public PayStub(WorkPlaceEmployee workPlaceEmployee, Long payPerHour, Long weeklyHolidayTime, BigDecimal tax) {
        this.workPlaceEmployee = workPlaceEmployee;
        this.status = PayStubStatus.READY;
        this.payPerHour = payPerHour;
        this.employeeSign = Boolean.FALSE;
        this.weeklyHolidayTime = weeklyHolidayTime;
        this.tax = tax;
    }
}
