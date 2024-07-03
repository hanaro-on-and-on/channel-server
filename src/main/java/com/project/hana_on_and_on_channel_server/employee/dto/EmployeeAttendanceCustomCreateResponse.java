package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;

public record EmployeeAttendanceCustomCreateResponse(
        Long customAttendanceMemoId
) {
    public static EmployeeAttendanceCustomCreateResponse fromEntity(CustomAttendanceMemo customAttendanceMemo){
        return new EmployeeAttendanceCustomCreateResponse(customAttendanceMemo.getCustomAttendanceMemoId());
    }
}
