package com.project.hana_on_and_on_channel_server.owner.dto;
import com.project.hana_on_and_on_channel_server.owner.vo.BusinessInfo;
import com.project.hana_on_and_on_channel_server.owner.vo.BusinessInfoList;

public record OwnerWorkPlaceCheckRegistrationNumberResponse(
        String result
) {
    public static OwnerWorkPlaceCheckRegistrationNumberResponse fromEntity(BusinessInfoList businessInfoList) {
        BusinessInfo businessInfo = businessInfoList.getData().get(0);
        return new OwnerWorkPlaceCheckRegistrationNumberResponse(
                businessInfo.getTax_type_cd() == "" ? businessInfo.getTax_type() : "valid"
        );
    }

}
