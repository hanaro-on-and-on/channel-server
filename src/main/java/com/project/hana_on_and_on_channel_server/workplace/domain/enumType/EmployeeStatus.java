package com.project.hana_on_and_on_channel_server.workplace.domain.enumType;

import lombok.Getter;

@Getter
public enum EmployeeStatus {

    WORKING("근로 중"),
    QUIT("퇴사"),
    ;

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }
}
