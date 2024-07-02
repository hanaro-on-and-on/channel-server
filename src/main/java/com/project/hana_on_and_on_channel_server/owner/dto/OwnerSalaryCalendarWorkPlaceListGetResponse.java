package com.project.hana_on_and_on_channel_server.owner.dto;

import java.util.List;

public record OwnerSalaryCalendarWorkPlaceListGetResponse(
        Integer currentPayment,
        Integer totalPayment,
        List<OwnerSalaryCalendarEmployeeListGetResponse> ownerSalaryCalendarEmployeeListGetResponseList
) {

    public static OwnerSalaryCalendarWorkPlaceListGetResponse fromEntity(
            Integer currentPayment,
            Integer totalPayment,
            List<OwnerSalaryCalendarEmployeeListGetResponse> ownerSalaryCalendarEmployeeListGetResponseList
    ){
        return new OwnerSalaryCalendarWorkPlaceListGetResponse(currentPayment, totalPayment, ownerSalaryCalendarEmployeeListGetResponseList);
    }
}
