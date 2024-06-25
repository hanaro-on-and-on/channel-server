package com.project.hana_on_and_on_channel_server.common.exception;

public class NetworkIOException extends RuntimeException {

    public NetworkIOException() {
        super("Network IO Problem occurred");
    }

    public NetworkIOException(String message) {
        super(message);
    }
}
