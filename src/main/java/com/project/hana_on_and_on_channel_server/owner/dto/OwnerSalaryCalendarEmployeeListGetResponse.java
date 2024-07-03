package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;

import java.util.List;

public record OwnerSalaryCalendarEmployeeListGetResponse(
        String workPlaceName,
        String workPlaceColorCode,
        String attendDate,
        Integer payment,
        Integer employeeSize,
        List<OwnerSalaryCalendarEmployeeGetResponse> employeeList
) {
    public static OwnerSalaryCalendarEmployeeListGetResponse fromEntity(WorkPlace workPlace, String attendDate, Integer payment, List<OwnerSalaryCalendarEmployeeGetResponse> employeeResponseList) {
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new OwnerSalaryCalendarEmployeeListGetResponse(
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                attendDate,
                payment,
                employeeResponseList.size(),
                employeeResponseList
        );
    }
}
