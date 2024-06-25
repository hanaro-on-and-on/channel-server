package com.project.hana_on_and_on_channel_server.common.exception;

public class JsonElementNotFoundException extends RuntimeException{

    public JsonElementNotFoundException() {
        super("Could not found json element");
    }

    public JsonElementNotFoundException(String message) {
        super(message);
    }
}

