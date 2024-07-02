package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceTotalGetResponse;

import java.util.List;

public record OwnerWorkPlaceEmployeeListGetResponse(
        List<OwnerWorkPlaceGetResponse> ownerWorkPlaceGetResponseList
) {
}
