package com.project.hana_on_and_on_channel_server.employee.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class EmployeeDuplicatedException extends ValueInvalidException {
    public EmployeeDuplicatedException() {
        super("Duplicated Employee.");
    }
}
