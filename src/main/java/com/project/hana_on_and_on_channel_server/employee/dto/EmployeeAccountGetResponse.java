package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;

public record EmployeeAccountGetResponse(
        String accountNumber,
        String phoneNumber,
        String username
) {

    public static EmployeeAccountGetResponse fromEntity(Employee employee){
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return new EmployeeAccountGetResponse(
                employee.getAccountNumber(),
                employee.getPhoneNumber(),
                employee.getEmployeeNm()
        );
    }
}
