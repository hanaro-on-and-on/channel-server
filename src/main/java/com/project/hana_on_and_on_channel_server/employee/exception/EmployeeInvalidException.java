package com.project.hana_on_and_on_channel_server.employee.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class EmployeeInvalidException extends ValueInvalidException {
    public EmployeeInvalidException(Long id) {
        super("Invalid Employee. employeeId:" + id);
    }
}
