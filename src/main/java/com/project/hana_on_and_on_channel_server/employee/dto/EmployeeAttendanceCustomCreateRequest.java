package com.project.hana_on_and_on_channel_server.employee.dto;

import java.time.LocalDateTime;

public record EmployeeAttendanceCustomCreateRequest(
        Long customWorkPlaceId,
        Long payPerHour,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer restMinute
) {
}
