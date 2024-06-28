package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;

public record EmployeeWorkPlaceCustomCreateResponse(Long customWorkPlaceId) {

    public static EmployeeWorkPlaceCustomCreateResponse fromEntity(CustomWorkPlace customWorkPlace){
        return new EmployeeWorkPlaceCustomCreateResponse(customWorkPlace.getCustomWorkPlaceId());
    }
}
