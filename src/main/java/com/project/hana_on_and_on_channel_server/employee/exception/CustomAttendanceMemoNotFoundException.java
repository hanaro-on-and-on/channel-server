package com.project.hana_on_and_on_channel_server.employee.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class CustomAttendanceMemoNotFoundException extends EntityNotFoundException {
    public CustomAttendanceMemoNotFoundException() {
        super("Could not found CustomAttendanceMemo");
    }
}