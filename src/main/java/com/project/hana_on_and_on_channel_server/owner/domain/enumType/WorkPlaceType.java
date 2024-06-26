package com.project.hana_on_and_on_channel_server.owner.domain.enumType;

import lombok.Getter;

@Getter
public enum WorkPlaceType {

    SMALL_BUSINESS("5인 미만"),
    LARGE_BUSINESS("5인 이상");

    private final String description;

    WorkPlaceType(String description) {
        this.description = description;
    }
}
