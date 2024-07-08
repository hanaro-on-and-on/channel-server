package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;

public record AttendanceCheckOutResponse(Long attendanceId, boolean success) {

    public static AttendanceCheckOutResponse fromEntity(Attendance attendance, boolean success) {
        return new AttendanceCheckOutResponse(attendance.getAttendanceId(), success);
    }
}