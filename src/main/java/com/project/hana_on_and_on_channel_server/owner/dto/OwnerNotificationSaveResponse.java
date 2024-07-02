package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.exception.NotificationNotFoundException;

import java.time.LocalDateTime;

public record OwnerNotificationSaveResponse(
        Long notificationId
) {
    public static OwnerNotificationSaveResponse fromEntity(Notification notification){
        if (notification == null) {
            throw new NotificationNotFoundException();
        }
        return new OwnerNotificationSaveResponse(
                notification.getNotificationId()
        );
    }
}
