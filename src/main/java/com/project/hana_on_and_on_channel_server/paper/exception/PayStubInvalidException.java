package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class PayStubInvalidException extends ValueInvalidException {
    public PayStubInvalidException() {
        super("Invalid PayStub");
    }

    public PayStubInvalidException(Long id) {
        super("Invalid PayStub "+id);
    }
}
