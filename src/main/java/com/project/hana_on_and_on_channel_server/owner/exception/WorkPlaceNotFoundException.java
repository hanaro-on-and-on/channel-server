package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class WorkPlaceNotFoundException extends EntityNotFoundException {

    public WorkPlaceNotFoundException() {
        super("Could not found WorkPlace");
    }

    public WorkPlaceNotFoundException(Long id) {
        super("Could not found WorkPlace"+id);
    }
}
