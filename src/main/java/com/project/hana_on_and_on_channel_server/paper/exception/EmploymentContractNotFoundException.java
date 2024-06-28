package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class EmploymentContractNotFoundException extends EntityNotFoundException {
    public EmploymentContractNotFoundException() {
        super("Could not found EmploymentContract");
    }
    public EmploymentContractNotFoundException(Long id) {
        super("Could not found EmploymentContract"+id);
    }
}
