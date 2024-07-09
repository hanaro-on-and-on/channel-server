package com.project.hana_on_and_on_channel_server.account.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super("Could not found user");
    }

    public UserNotFoundException(Long id) {
        super("Could not found user"+id);
    }
}
