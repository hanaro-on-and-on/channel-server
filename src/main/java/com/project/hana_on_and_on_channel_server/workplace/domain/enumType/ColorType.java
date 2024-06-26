package com.project.hana_on_and_on_channel_server.workplace.domain.enumType;

import lombok.Getter;

@Getter
public enum ColorType {

    BLACK(01), RED(02), GREEN(03), YELLOW(04), BLUE(05), MAGENTA(06), WHITE(07);

    private int code;

    ColorType(int code) {
        this.code = code;
    }
}
