package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class EmploymentContractInvalidException extends ValueInvalidException {
    public EmploymentContractInvalidException() {
        super("Invalid EmploymentContract");
    }

    public EmploymentContractInvalidException(Long id) {
        super("Invalid EmploymentContract. employmentContractId"+id);
    }
}
