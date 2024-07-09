package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.WorkPlaceType;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;
import java.time.LocalDate;

public record OwnerWorkPlaceUpsertRequest(
        String workPlaceNm,
        String address,
        GeoPoint location,
        String businessRegistrationNumber,
        String openingDate, // yyyymmdd
        WorkPlaceType workPlaceType,
        String colorTypeCode,
        Long ownerId
) {

}
