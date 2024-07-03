package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class WorkPlaceEmployeeInvalidException extends ValueInvalidException {
    public WorkPlaceEmployeeInvalidException() {
        super("Invalid WorkPlaceEmployee");
    }

    public WorkPlaceEmployeeInvalidException(Long id) {
        super("Invalid WorkPlaceEmployee. "+id);
    }
}
