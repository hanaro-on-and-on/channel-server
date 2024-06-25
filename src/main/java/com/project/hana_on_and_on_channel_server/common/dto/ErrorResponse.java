package com.project.hana_on_and_on_channel_server.common.dto;

import lombok.Getter;
@Getter
public class ErrorResponse {

    String type;
    String message;

    public ErrorResponse(Throwable e) {
        this.type = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }
}
