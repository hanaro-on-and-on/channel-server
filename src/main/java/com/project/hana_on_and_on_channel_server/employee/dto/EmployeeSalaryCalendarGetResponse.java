package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomAttendanceMemoNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;

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
    // 연결 근무지
    public static EmployeeSalaryCalendarGetResponse fromEntity(WorkPlaceEmployee workPlaceEmployee, Attendance attendance, Integer payment){
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        if (attendance == null) {
            throw new AttendanceNotFoundException();
        }
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeSalaryCalendarGetResponse(
                true,
                attendance.getAttendanceId(),
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                attendance.getAttendDate(),
                attendance.getAttendanceType(),
                attendance.getAttendanceType() == AttendanceType.REAL ? attendance.getRealStartTime() : attendance.getStartTime(),
                attendance.getAttendanceType() == AttendanceType.REAL ? attendance.getRealEndTime() : attendance.getEndTime(),
                attendance.getRestMinute(),
                payment
        );
    }

    // 수동 근무지
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
