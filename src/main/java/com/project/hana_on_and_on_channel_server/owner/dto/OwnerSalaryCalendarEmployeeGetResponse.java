package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;

import java.time.LocalDateTime;

public record OwnerSalaryCalendarEmployeeGetResponse(
        Long attendanceId,
        String employeeName,
        LocalDateTime startTime,
        LocalDateTime endDate,
        Integer restMinute,
        Integer payment
) {
    public static OwnerSalaryCalendarEmployeeGetResponse fromEntity(Attendance attendance, WorkPlaceEmployee workPlaceEmployee, Integer payment) {
        if (attendance == null) {
            throw new AttendanceNotFoundException();
        }
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        Employee employee = workPlaceEmployee.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return new OwnerSalaryCalendarEmployeeGetResponse(
                attendance.getAttendanceId(),
                employee.getEmployeeNm(),
                attendance.getStartTime(),
                attendance.getEndTime(),
                attendance.getRestMinute(),
                payment
        );
    }
}
