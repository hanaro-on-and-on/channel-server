package com.project.hana_on_and_on_channel_server.owner.exception;

import com.project.hana_on_and_on_channel_server.common.exception.EntityNotFoundException;

public class ColorTypeNotFoundException extends EntityNotFoundException {

    public ColorTypeNotFoundException() {
        super("Could not found ColorType");
    }

    public ColorTypeNotFoundException(Long id) {
        super("Could not found ColorType"+id);
    }
}
