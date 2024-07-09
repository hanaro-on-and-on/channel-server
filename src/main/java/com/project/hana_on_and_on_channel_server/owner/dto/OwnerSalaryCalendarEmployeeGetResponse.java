package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

public record OwnerSalaryCalendarEmployeeGetResponse(
        Long attendanceId,
        Long workPlaceEmployeeId,
        String employeeName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer restMinute,
        Integer payment
) {
    public static Optional<OwnerSalaryCalendarEmployeeGetResponse> fromEntity(Attendance attendance, WorkPlaceEmployee workPlaceEmployee, Integer payment) {
        if (attendance == null) {
            throw new AttendanceNotFoundException();
        }
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        Employee employee = workPlaceEmployee.getEmployee();
        if (employee == null) {
            return Optional.empty();
        }
        return Optional.of(new OwnerSalaryCalendarEmployeeGetResponse(
                attendance.getAttendanceId(),
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                employee.getEmployeeNm(),
                attendance.getStartTime(),
                attendance.getEndTime(),
                attendance.getRestMinute(),
                payment
        ));
    }
}
