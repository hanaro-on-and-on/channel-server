package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;

public record SalaryTransferReserveResponse(Long salaryTransferReserveId) {

    public static SalaryTransferReserveResponse fromEntity(SalaryTransferReserve entity){
        return new SalaryTransferReserveResponse(entity.getSalaryTransferReserveId());
    }
}
