package com.project.hana_on_and_on_channel_server.paper.projection;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

import java.time.LocalDateTime;

public interface EmploymentContractSummary {
    Long getEmploymentContractId();
    ColorType getColorTypeCd();
    String getWorkPlaceNm();
    LocalDateTime getEmploymentContractCreatedAt();
}
