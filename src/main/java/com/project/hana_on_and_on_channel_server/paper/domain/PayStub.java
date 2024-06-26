package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(nullable = false)
    private Long basicHour;

    @Column(nullable = false)
    private Long overHour;

    @Column(nullable = false, length = 255)
    private String employeeSign;

    @Column(nullable = false)
    private Long weeklyHolidayTime;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal tax;

    @Builder
    public PayStub(WorkPlaceEmployee workPlaceEmployee, Boolean status, Long payPerHour,
        Long basicHour,
        Long overHour, String employeeSign, Long weeklyHolidayTime, BigDecimal tax) {
        this.workPlaceEmployee = workPlaceEmployee;
        this.status = status;
        this.payPerHour = payPerHour;
        this.basicHour = basicHour;
        this.overHour = overHour;
        this.employeeSign = employeeSign;
        this.weeklyHolidayTime = weeklyHolidayTime;
        this.tax = tax;
    }
}
