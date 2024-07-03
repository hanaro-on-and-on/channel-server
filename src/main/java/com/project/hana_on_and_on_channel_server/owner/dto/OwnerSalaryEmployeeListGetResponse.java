package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;

import java.util.List;

public record OwnerSalaryEmployeeListGetResponse(
        Long id,
        String workPlaceName,
        String workPlaceColor,
        Integer payment,
        List<OwnerSalaryEmployeeGetResponse> employeeList
) {

    public static OwnerSalaryEmployeeListGetResponse fromEntity(WorkPlace workPlace, Integer payment, List<OwnerSalaryEmployeeGetResponse> ownerSalaryEmployeeGetResponseList
    ){
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new OwnerSalaryEmployeeListGetResponse(
                workPlace.getWorkPlaceId(),
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                payment,
                ownerSalaryEmployeeGetResponseList
        );
    }
}