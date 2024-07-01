package com.project.hana_on_and_on_channel_server.paper.domain.enumType;

import lombok.Getter;

@Getter
public enum PayStubStatus {

    READY("간편지급"),
    SIGN("서명요청"),
    WAITING("지급대기"),
    COMPLETED("지급완료")
    ;

    private final String description;
    PayStubStatus(String description) { this.description = description; }
}
