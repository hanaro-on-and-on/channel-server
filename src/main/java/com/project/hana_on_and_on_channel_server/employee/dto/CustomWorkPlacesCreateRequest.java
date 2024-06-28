package com.project.hana_on_and_on_channel_server.employee.dto;

public record CustomWorkPlacesCreateRequest(
        String customWorkPlaceNm,
        Long payPerHour
) {
}
