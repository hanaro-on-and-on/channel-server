package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;

public record OwnerWorkPlaceGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCode,
        String employeeName
) {
    public static OwnerWorkPlaceGetResponse fromEntity(WorkPlaceEmployee workPlaceEmployee){
        return new OwnerWorkPlaceGetResponse(
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                workPlaceEmployee.getWorkPlace().getWorkPlaceNm(),
                workPlaceEmployee.getColorType().getCode(),
                workPlaceEmployee.getEmployee().getEmployeeNm()
        );
    }
}
