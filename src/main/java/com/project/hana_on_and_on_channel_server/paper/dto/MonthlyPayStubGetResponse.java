package com.project.hana_on_and_on_channel_server.paper.dto;

import lombok.Builder;

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
        Long basicHour,
        Long basicPay,
        Long overHour,
        Long overPay,
        Long weeklyHolidayTime,
        Long weeklyHolidayPay,
        Double taxRate,
        Long taxPay
) {
}
