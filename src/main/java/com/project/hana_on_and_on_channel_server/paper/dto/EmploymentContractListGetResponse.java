package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;

import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDDashFormat;

public record EmploymentContractListGetResponse(Long employmentContractId, String colorTypeCd, String workPlaceNm, String employmentContractCreatedAt) {

    public static EmploymentContractListGetResponse fromProjection(EmploymentContractSummary projection){
        return new EmploymentContractListGetResponse(projection.getEmploymentContractId(), projection.getColorTypeCd(), projection.getWorkPlaceNm(), localDateTimeToYMDDashFormat(projection.getEmploymentContractCreatedAt()));
    }
}
