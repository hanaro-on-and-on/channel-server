package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;

public record EmployeeAccountUpsertResponse(Long employeeId) {

    public static EmployeeAccountUpsertResponse fromEntity(Employee employee){
        return new EmployeeAccountUpsertResponse(employee.getEmployeeId());
    }
}
