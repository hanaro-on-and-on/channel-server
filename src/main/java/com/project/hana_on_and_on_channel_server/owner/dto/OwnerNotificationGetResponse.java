package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.exception.NotificationNotFoundException;

import java.time.LocalDateTime;

public record OwnerNotificationGetResponse(
        Long notificationId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OwnerNotificationGetResponse fromEntity(Notification notification){
        if (notification == null) {
            throw new NotificationNotFoundException();
        }
        return new OwnerNotificationGetResponse(
                notification.getNotificationId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getCreatedAt(),
                notification.getUpdatedAt()
        );
    }
}
