package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;

public record CustomWorkPlacesCreateResponse(Long customWorkPlaceId) {

    public static CustomWorkPlacesCreateResponse fromEntity(CustomWorkPlace customWorkPlace){
        return new CustomWorkPlacesCreateResponse(customWorkPlace.getCustomWorkPlaceId());
    }
}
