package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class OwnerNotFoundException extends EntityNotFoundException {

    public OwnerNotFoundException() {
        super("Could not found Owner");
    }

    public OwnerNotFoundException(Long id) {
        super("Could not found Owner"+id);
    }
}
