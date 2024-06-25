package com.project.hana_on_and_on_channel_server.common.exception;

public class ValueInvalidException extends RuntimeException{

    public ValueInvalidException() {
        super("Invalid value");
    }

    public ValueInvalidException(String message) {
        super(message);
    }
}
