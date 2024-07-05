package com.project.hana_on_and_on_channel_server.paper.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record EmploymentContractUpsertRequest(
        String employeeNm,
        String employeeAddress,
        String employeePhone,
        LocalDate workStartDate,
        LocalDate workEndDate,
        String workSite,
        String workDetail,
        List<WorkTimeRequest> workTimes,
        Long payPerHour,
        Long paymentDay,
        String restDayOfWeek,
        Long bonusAmount,
        Long otherAllowancesAmount,
        String otherAllowancesName,
        Long overTimeRate
) {
    public record WorkTimeRequest(String workDayOfWeek, @JsonFormat(pattern = "HH:mm") LocalTime workStartTime, @JsonFormat(pattern = "HH:mm") LocalTime workEndTime, @JsonFormat(pattern = "HH:mm") LocalTime restStartTime, @JsonFormat(pattern = "HH:mm") LocalTime restEndTime){}
}
