package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;

public record AttendanceCheckInResponse(Long attendanceId) {

    public static AttendanceCheckInResponse fromEntity(Attendance attendance) {
        return new AttendanceCheckInResponse(attendance.getAttendanceId());
    }
}
