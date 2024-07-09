package com.project.hana_on_and_on_channel_server.owner.dto;
import com.project.hana_on_and_on_channel_server.owner.vo.BusinessInfoData;
import com.project.hana_on_and_on_channel_server.owner.vo.BusinessInfoResponse;

import java.util.Objects;

public record OwnerWorkPlaceCheckRegistrationNumberResponse(
        Boolean valid,
        String b_nm,
        String b_stt,
        String b_adr
) {
    public static OwnerWorkPlaceCheckRegistrationNumberResponse fromEntity(BusinessInfoResponse businessInfoResponse, String b_adr) {
        BusinessInfoData businessInfoData = businessInfoResponse.getData().get(0);
        Boolean valid = Objects.equals(businessInfoData.getValid(), "01");
        return new OwnerWorkPlaceCheckRegistrationNumberResponse(
                valid,
                valid ? businessInfoData.getRequest_param().getB_nm() : null,
                valid ? businessInfoData.getStatus().getB_stt() : null,
                b_adr
        );
    }

}
