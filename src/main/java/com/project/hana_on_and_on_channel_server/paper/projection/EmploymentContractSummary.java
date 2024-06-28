package com.project.hana_on_and_on_channel_server.paper.projection;

import java.time.LocalDateTime;

public interface EmploymentContractSummary {
    Long getEmploymentContractId();
    String getWorkPlaceNm();
    LocalDateTime getEmploymentContractCreatedAt();
}
