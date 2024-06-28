package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record EmployeeWorkPlaceInvitationListGetResponse(
        List<EmployeeWorkPlaceInvitationGetResponse> workPlacesInvitaionsGetResponseList
) {

    public static EmployeeWorkPlaceInvitationListGetResponse fromEntity(List<EmployeeWorkPlaceInvitationGetResponse> employeeWorkPlaceInvitationGetResponseList){
        return new EmployeeWorkPlaceInvitationListGetResponse(employeeWorkPlaceInvitationGetResponseList);
    }
}
