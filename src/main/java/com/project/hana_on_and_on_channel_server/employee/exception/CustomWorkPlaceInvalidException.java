package com.project.hana_on_and_on_channel_server.employee.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class CustomWorkPlaceInvalidException extends ValueInvalidException {
    public CustomWorkPlaceInvalidException() {
        super("Invalid CustomWorkPlace");
    }

    public CustomWorkPlaceInvalidException(Long id) {
        super("Invalid CustomWorkPlace. "+id);
    }
}