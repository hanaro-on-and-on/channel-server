package com.project.hana_on_and_on_channel_server.employee.exception;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {
    public EmployeeNotFoundException() {
        super("Could not found Employee");
    }

    public EmployeeNotFoundException(Long id) {
        super("Could not found Employee"+id);
    }
}
