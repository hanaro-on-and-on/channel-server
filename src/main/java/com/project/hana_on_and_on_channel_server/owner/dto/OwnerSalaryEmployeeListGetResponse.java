package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

import java.util.List;

public record OwnerSalaryEmployeeListGetResponse(
        Long id,
        String workPlaceName,
        ColorType workPlaceColor,
        Integer payment,
        List<OwnerSalaryEmployeeGetResponse> ownerSalaryEmployeeGetResponseList
) {

    public static OwnerSalaryEmployeeListGetResponse fromEntity(
            Long id,
            String workPlaceName,
            ColorType workPlaceColor,
            Integer payment,
            List<OwnerSalaryEmployeeGetResponse> ownerSalaryEmployeeGetResponseList
    ){
        return new OwnerSalaryEmployeeListGetResponse(id, workPlaceName, workPlaceColor, payment, ownerSalaryEmployeeGetResponseList);
    }
}