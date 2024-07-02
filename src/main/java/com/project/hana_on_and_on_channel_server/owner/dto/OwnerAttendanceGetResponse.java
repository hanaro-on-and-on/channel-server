package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;

import java.time.LocalDateTime;

public record OwnerAttendanceGetResponse(
        Long attendanceId,
        Long workPlaceEmployeeId,
        Long payPerHour,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer restMinute
) {
    public static OwnerAttendanceGetResponse fromEntity(Attendance attendance){
        LocalDateTime startTime = attendance.getAttendanceType() == AttendanceType.EXPECT ? attendance.getStartTime() : attendance.getRealStartTime();
        LocalDateTime endTime = attendance.getAttendanceType() == AttendanceType.EXPECT ? attendance.getEndTime() : attendance.getRealEndTime();
        return new OwnerAttendanceGetResponse(attendance.getAttendanceId(), attendance.getWorkPlaceEmployee().getWorkPlaceEmployeeId(), attendance.getPayPerHour(), startTime, endTime, attendance.getRestMinute());
    }
}
