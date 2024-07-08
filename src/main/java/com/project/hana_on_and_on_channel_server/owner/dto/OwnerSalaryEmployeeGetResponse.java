package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

public record OwnerSalaryEmployeeGetResponse(
        Long workPlaceEmployeeId,
        String employeeName,
        LocalDate workStartDate,
        Integer payment
) {
    public static Optional<OwnerSalaryEmployeeGetResponse> fromEntity(WorkPlaceEmployee workPlaceEmployee, Integer payment){
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        Employee employee = workPlaceEmployee.getEmployee();
        if (employee == null) {
            return Optional.empty();
        }
        return Optional.of(new OwnerSalaryEmployeeGetResponse(
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                employee.getEmployeeNm(),
                workPlaceEmployee.getWorkStartDate(),
                payment
        ));
    }

}