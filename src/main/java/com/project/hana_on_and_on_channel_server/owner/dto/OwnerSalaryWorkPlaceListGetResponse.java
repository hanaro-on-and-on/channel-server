package com.project.hana_on_and_on_channel_server.owner.dto;

import java.util.List;

public record OwnerSalaryWorkPlaceListGetResponse(
        Integer year,
        Integer month,
        Integer totalPayment,
        List<OwnerSalaryEmployeeListGetResponse> ownerSalaryEmployeeListGetResponseList
) {

    public static OwnerSalaryWorkPlaceListGetResponse fromEntity(
            Integer year,
            Integer month,
            Integer totalPayment,
            List<OwnerSalaryEmployeeListGetResponse> ownerSalaryEmployeeListGetResponseList
    ){
        return new OwnerSalaryWorkPlaceListGetResponse(year, month, totalPayment, ownerSalaryEmployeeListGetResponseList);
    }
}