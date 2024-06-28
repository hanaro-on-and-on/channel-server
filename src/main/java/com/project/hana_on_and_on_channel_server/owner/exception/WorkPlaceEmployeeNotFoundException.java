package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class WorkPlaceEmployeeNotFoundException extends EntityNotFoundException {

    public WorkPlaceEmployeeNotFoundException() {
        super("Could not found WorkPlace-Employee");
    }

    public WorkPlaceEmployeeNotFoundException(Long id) {
        super("Could not found WorkPlace-Employee"+id);
    }
}
