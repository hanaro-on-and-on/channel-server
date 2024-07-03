package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record EmployeeWorkPlaceListGetResponse(
        List<EmployeeWorkPlaceGetResponse> invitatedWorkPlaceList,
        List<EmployeeWorkPlaceGetResponse> connectedWorkPlaceList,
        List<EmployeeWorkPlaceGetResponse> customWorkPlaceList
) {
}
