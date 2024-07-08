package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.PayStubWorkTime;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record MonthlyPayStubGetResponse(
        Long payStubId,
        Long workPlaceEmployeeId,
        int year,
        int month,
        String status,
        Long salary,
        Long totalPay,
        Long totalTaxPay,
        Long paymentDay,
        Long payPerHour,
        List<Basic> basic,
        Long weeklyHolidayTime,
        Long weeklyHolidayPay,
        Double taxRate,
        Long taxPay
) {
    public record Basic(Long payPerHour, Long basicHour, Long basicPay, Long overHour, Long overPay){
        public static List<Basic> fromEntity(List<PayStubWorkTime> payStubWorkTimeList) {
            return payStubWorkTimeList.stream()
                    .map(payStubWorkTime -> new Basic(
                            payStubWorkTime.getPayPerHour(),
                            payStubWorkTime.getBasicHour(),
                            payStubWorkTime.calcBasicPay(),
                            payStubWorkTime.getOverHour(),
                            payStubWorkTime.calcOverPay()))
                    .toList();
        }
    }
}
