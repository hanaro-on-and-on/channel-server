package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record WorkPlacesInvitationsListGetResponse(
        List<WorkPlacesInvitationsGetResponse> workPlacesInvitaionsGetResponseList
) {

    public static WorkPlacesInvitationsListGetResponse fromEntity(List<WorkPlacesInvitationsGetResponse> workPlacesInvitationsGetResponseList){
        return new WorkPlacesInvitationsListGetResponse(workPlacesInvitationsGetResponseList);
    }
}
