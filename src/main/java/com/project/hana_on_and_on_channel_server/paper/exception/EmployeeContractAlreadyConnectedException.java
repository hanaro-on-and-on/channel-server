package com.project.hana_on_and_on_channel_server.paper.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class EmployeeContractAlreadyConnectedException extends ValueInvalidException {
    public EmployeeContractAlreadyConnectedException() {
        super("Employee Contract Already Connected");
    }
}
