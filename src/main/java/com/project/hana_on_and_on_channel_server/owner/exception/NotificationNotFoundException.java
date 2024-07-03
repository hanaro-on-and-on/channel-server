package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class NotificationNotFoundException extends EntityNotFoundException {

    public NotificationNotFoundException() {
        super("Could not found Notification");
    }

    public NotificationNotFoundException(Long id) {
        super("Could not found Notification"+id);
    }
}
