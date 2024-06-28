package com.project.hana_on_and_on_channel_server.attendance.exception;

import com.project.hana_on_and_on_channel_server.common.exception.ValueInvalidException;

public class AttendanceDuplicatedException extends ValueInvalidException {

    public AttendanceDuplicatedException() {
        super("Duplicated Attendance.");
    }
}
