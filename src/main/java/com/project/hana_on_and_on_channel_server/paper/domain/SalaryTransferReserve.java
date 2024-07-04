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
    @Column(name = "salary_transfer_reserve_id")
    private Long salaryTransferReserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_stub_id")
    private PayStub payStub;

    @Column(nullable = false, name = "total_pay")
    private Long totalPay;

    @Column(nullable = false, name = "reserve_date")
    private String reserveDate;

    @Column(nullable = false, name = "sender_nm")
    private String senderNm;

    @Column(nullable = false, name = "sender_account_number")
    private String senderAccountNumber;

    @Column(nullable = false, name = "receiver_nm")
    private String receiverNm;

    @Column(nullable = false, name = "receiver_account_number")
    private String receiverAccountNumber;

    @Column(nullable = false, name = "transfer_yn")
    private Boolean transferYn;

    public void completeTransfer(){
        this.transferYn = Boolean.TRUE;
    }

    @Builder
    public SalaryTransferReserve(PayStub payStub, Long totalPay, String reserveDate, String senderNm, String senderAccountNumber, String receiverNm, String receiverAccountNumber) {
        this.payStub = payStub;
        this.totalPay = totalPay;
        this.reserveDate = reserveDate;
        this.senderNm = senderNm;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverNm = receiverNm;
        this.receiverAccountNumber = receiverAccountNumber;
        this.transferYn = Boolean.FALSE;
    }
}
