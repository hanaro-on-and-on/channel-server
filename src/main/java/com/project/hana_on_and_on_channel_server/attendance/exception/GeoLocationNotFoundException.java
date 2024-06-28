package com.project.hana_on_and_on_channel_server.attendance.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class GeoLocationNotFoundException extends EntityNotFoundException {

    public GeoLocationNotFoundException() {
        super("Could not found GeoLocation");
    }

    public GeoLocationNotFoundException(Long id) {
        super("Could not found GeoLocation"+id);
    }
}
