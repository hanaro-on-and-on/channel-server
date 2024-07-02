package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;

public record NotificationGetResponse(Long notificationId, String title, String content) {

    public static NotificationGetResponse fromEntity(Notification notification){
        return new NotificationGetResponse(notification.getNotificationId(), notification.getTitle(), notification.getContent());
    }
}
