package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class OwnerDuplicatedException extends ValueInvalidException {

    public OwnerDuplicatedException() {
        super("Owner Duplicated");
    }

    public OwnerDuplicatedException(Long id) {
        super("Owner Duplicated"+id);
    }
}
