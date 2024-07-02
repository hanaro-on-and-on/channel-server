package com.project.hana_on_and_on_channel_server.attendance.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class AttendanceNotFoundException extends EntityNotFoundException {

    public AttendanceNotFoundException() {
        super("Could not found Attendance");
    }

    public AttendanceNotFoundException(Long id) {
        super("Could not found Attendance"+id);
    }
}
