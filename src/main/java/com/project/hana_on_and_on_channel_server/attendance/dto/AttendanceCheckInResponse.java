package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;

public record AttendanceCheckInResponse(Long attendanceId, boolean success) {

    public static AttendanceCheckInResponse fromEntity(Attendance attendance, boolean success) {
        return new AttendanceCheckInResponse(attendance.getAttendanceId(), success);
    }
}
