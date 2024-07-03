package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;

public record OwnerAccountGetResponse(
        String accountNumber,
        String phoneNumber,
        String username
) {

    public static OwnerAccountGetResponse fromEntity(Owner owner){
        if (owner == null) {
            throw new OwnerNotFoundException();
        }
        return new OwnerAccountGetResponse(
                owner.getAccountNumber(),
                owner.getPhoneNumber(),
                owner.getOwnerNm()
        );
    }
}
