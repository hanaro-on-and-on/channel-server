package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class PayStubNotFoundException extends EntityNotFoundException {
    public PayStubNotFoundException() {
        super("Could not found PayStub");
    }
}
