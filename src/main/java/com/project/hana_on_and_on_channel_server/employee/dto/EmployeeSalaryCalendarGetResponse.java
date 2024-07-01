package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeSalaryCalendarGetResponse(
        Boolean isConnected,
        String workPlaceName,
        ColorType workPlaceColor,
        String attendDate,
        AttendanceType attendanceType,
        LocalDateTime startTime,
        LocalDateTime endDate,
        Integer restMinute,
        Integer payment
) {
}
