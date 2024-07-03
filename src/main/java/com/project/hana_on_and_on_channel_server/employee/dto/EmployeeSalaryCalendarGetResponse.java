package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomAttendanceMemoNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EmployeeSalaryCalendarGetResponse(
        Boolean isConnected,
        Long id,
        String workPlaceName,
        String workPlaceColorCode,
        String attendDate,
        AttendanceType attendanceType,
        LocalDateTime startTime,
        LocalDateTime endDate,
        Integer restMinute,
        Integer payment
) {
    public static EmployeeSalaryCalendarGetResponse fromEntity(CustomWorkPlace customWorkPlace, CustomAttendanceMemo customAttendanceMemo, Integer payment){
        if (customWorkPlace == null) {
            throw new CustomWorkPlaceNotFoundException();
        }
        if (customAttendanceMemo == null) {
            throw new CustomAttendanceMemoNotFoundException();
        }
        ColorType colorType = customWorkPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeSalaryCalendarGetResponse(
                false,
                customAttendanceMemo.getCustomAttendanceMemoId(),
                customWorkPlace.getCustomWorkPlaceNm(),
                colorType.getCode(),
                customAttendanceMemo.getAttendDate(),
                AttendanceType.EXPECT,
                customAttendanceMemo.getStartTime(),
                customAttendanceMemo.getEndTime(),
                customAttendanceMemo.getRestMinute(),
                payment
        );
    }
}
