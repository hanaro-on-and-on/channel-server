package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="salary_transfer_reserve")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class SalaryTransferReserve extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryTransferReserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_stub_id")
    private PayStub payStub;

    @Column(nullable = false)
    private Long totalPay;

    @Column(nullable = false)
    private String reserveDate;

    @Column(nullable = false)
    private String senderNm;

    @Column(nullable = false)
    private String receiverNm;

    @Column(nullable = false)
    private Boolean transfer_yn;


    @Builder
    public SalaryTransferReserve(PayStub payStub, Long totalPay, String reserveDate, String senderNm, String receiverNm) {
        this.payStub = payStub;
        this.totalPay = totalPay;
        this.reserveDate = reserveDate;
        this.senderNm = senderNm;
        this.receiverNm = receiverNm;
        this.transfer_yn = Boolean.FALSE;
    }
}
