package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;

public record OwnerAccountUpsertResponse(Long ownerId) {

    public static OwnerAccountUpsertResponse fromEntity(Owner owner){
        return new OwnerAccountUpsertResponse(owner.getOwnerId());
    }
}
