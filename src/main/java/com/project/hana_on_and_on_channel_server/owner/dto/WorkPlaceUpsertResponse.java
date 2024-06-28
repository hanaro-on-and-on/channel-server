package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;

public record WorkPlaceUpsertResponse(Long workPlaceId) {

    public static WorkPlaceUpsertResponse fromEntity(WorkPlace workPlace) {
        return new WorkPlaceUpsertResponse(workPlace.getWorkPlaceId());
    }
}
