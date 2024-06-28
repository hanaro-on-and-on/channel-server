package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.WorkPlaceStatus;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.WorkPlaceType;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;
import java.time.LocalDate;

public record WorkPlaceUpsertRequest(String workPlaceNm, String address, GeoPoint location, String businessRegistrationNumber, LocalDate openingDate, WorkPlaceStatus workPlaceStatus, WorkPlaceType workPlaceType, ColorType colorType, Long ownerId) {

}
