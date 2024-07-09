package com.project.hana_on_and_on_channel_server.owner.domain.enumType;

import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import lombok.Getter;

@Getter
public enum ColorType {

    BLACK("01"), RED("02"), GREEN("03"), YELLOW("04"), BLUE("05"), MAGENTA("06"), WHITE("07");

    private String code;

    ColorType(String code) {
        this.code = code;
    }

    public static ColorType fromCode(String code) {
        for (ColorType colorType : values()) {
            if (colorType.getCode().equals(code)) {
                return colorType;
            }
        }
        throw new ColorTypeNotFoundException();
    }
}
