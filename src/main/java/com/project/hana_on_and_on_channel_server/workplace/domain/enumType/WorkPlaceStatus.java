package com.project.hana_on_and_on_channel_server.workplace.domain.enumType;

import lombok.Getter;

@Getter
public enum WorkPlaceStatus {

    OPERATING("운영중"),
    SUSPENDED("휴업"),
    CLOSED("폐업");

    private final String description;

    WorkPlaceStatus(String description) {
        this.description = description;
    }
}
