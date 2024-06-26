package com.project.hana_on_and_on_channel_server.attendance.domain;

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

@Entity(name="salary_transfer_history")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class SalaryTransferHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryTransferHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @Column(nullable = false)
    private Long basicPay;

    @Column(nullable = false)
    private Long overPay;

    @Column(nullable = false)
    private Long tax;

    @Builder
    public SalaryTransferHistory(Attendance attendance, Long basicPay, Long overPay, Long tax) {
        this.attendance = attendance;
        this.basicPay = basicPay;
        this.overPay = overPay;
        this.tax = tax;
    }
}
