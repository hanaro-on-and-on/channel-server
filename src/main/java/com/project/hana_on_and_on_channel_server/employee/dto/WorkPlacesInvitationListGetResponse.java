package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record WorkPlacesInvitationListGetResponse(
        List<WorkPlacesInvitationGetResponse> workPlacesInvitaionsGetResponseList
) {

    public static WorkPlacesInvitationListGetResponse fromEntity(List<WorkPlacesInvitationGetResponse> workPlacesInvitationGetResponseList){
        return new WorkPlacesInvitationListGetResponse(workPlacesInvitationGetResponseList);
    }
}
