package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;

public record EmploymentContractListGetResponse(Long employmentContractId, String workPlaceNm, String employmentContractCreatedAt) {

    public static EmploymentContractListGetResponse fromProjection(EmploymentContractSummary projection){
        return new EmploymentContractListGetResponse(projection.getEmploymentContractId(), projection.getWorkPlaceNm(), projection.getEmploymentContractCreatedAt().toString());
    }
}
