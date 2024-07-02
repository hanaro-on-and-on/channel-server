package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

import java.util.List;

public record OwnerSalaryCalendarEmployeeListGetResponse(
        String workPlaceName,
        ColorType workPlaceColor,
        String attendDate,
        Integer payment,
        Integer employeeSize,
        List<OwnerSalaryCalendarEmployeeGetResponse> ownerSalaryCalendarEmployeeGetResponseList
) {

    public static OwnerSalaryCalendarEmployeeListGetResponse fromEntity(
            String workPlaceName,
            ColorType workPlaceColor,
            String attendDate,
            Integer payment,
            Integer employeeSize,
            List<OwnerSalaryCalendarEmployeeGetResponse> ownerSalaryCalendarEmployeeGetResponseList
    ){
        return new OwnerSalaryCalendarEmployeeListGetResponse(workPlaceName, workPlaceColor, attendDate, payment, employeeSize, ownerSalaryCalendarEmployeeGetResponseList);
    }
}
