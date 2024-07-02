package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;

public record OwnerWorkPlaceUpsertResponse(Long workPlaceId) {

    public static OwnerWorkPlaceUpsertResponse fromEntity(WorkPlace workPlace) {
        return new OwnerWorkPlaceUpsertResponse(workPlace.getWorkPlaceId());
    }
}
