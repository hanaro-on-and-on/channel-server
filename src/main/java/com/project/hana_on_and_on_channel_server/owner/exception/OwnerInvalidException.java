package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class OwnerInvalidException extends ValueInvalidException {
    public OwnerInvalidException() {
        super("Invalid Owner");
    }

    public OwnerInvalidException(Long id) {
        super("Invalid Owner. userId"+id);
    }
}
