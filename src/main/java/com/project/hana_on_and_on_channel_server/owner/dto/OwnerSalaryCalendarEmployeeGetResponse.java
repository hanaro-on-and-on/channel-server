package com.project.hana_on_and_on_channel_server.owner.dto;

import java.time.LocalDateTime;

public record OwnerSalaryCalendarEmployeeGetResponse(
        Long attendanceId,
        String employeeName,
        LocalDateTime startTime,
        LocalDateTime endDate,
        Integer restMinute,
        Integer payment
) {
}
