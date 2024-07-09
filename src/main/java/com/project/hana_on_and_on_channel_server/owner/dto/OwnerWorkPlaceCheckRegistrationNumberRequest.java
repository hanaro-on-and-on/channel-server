package com.project.hana_on_and_on_channel_server.owner.dto;


public record OwnerWorkPlaceCheckRegistrationNumberRequest(
        String businessRegistrationNumber,
        String openDate, // yyyymmdd
        String businessName,
        String businessAddress
) {
}
