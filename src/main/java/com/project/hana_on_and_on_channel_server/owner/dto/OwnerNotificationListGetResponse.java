package com.project.hana_on_and_on_channel_server.owner.dto;

import java.util.List;

public record OwnerNotificationListGetResponse(
        List<OwnerNotificationGetResponse> ownerNotificationGetResponseList
) {
}