package com.project.hana_on_and_on_channel_server.employee.dto;

public record EmployeeWorkPlaceCustomCreateRequest(
        String customWorkPlaceNm,
        Long payPerHour
) {
}
