package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class EmploymentContractAlreadyConnectedException extends ValueInvalidException {
    public EmploymentContractAlreadyConnectedException() {
        super("Employee Contract Already Connected");
    }
}
