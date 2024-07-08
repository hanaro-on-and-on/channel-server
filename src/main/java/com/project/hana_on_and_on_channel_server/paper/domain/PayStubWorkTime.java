package com.project.hana_on_and_on_channel_server.paper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="pay_stub_work_time")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class PayStubWorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payStubWorkTimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_stub_id")
    private PayStub payStub;

    @Column(name = "pay_per_hour")
    private Long payPerHour;

    @Column(name = "basic_hour")
    private Long basicHour;

    @Column(name = "over_hour")
    private Long overHour;

    public Long calcTotalPay(){
        return this.calcBasicPay() + this.calcOverPay();
    }

    public Long calcBasicPay(){
        return (Long) (this.basicHour*this.payPerHour);
    }

    public Long calcOverPay(){
        return (long) (this.overHour*this.payPerHour*1.5);
    }

    @Builder
    public PayStubWorkTime(PayStub payStub, Long payPerHour, Long basicHour, Long overHour) {
        this.payStub = payStub;
        this.payPerHour = payPerHour;
        this.basicHour = basicHour;
        this.overHour = overHour;
    }
}
