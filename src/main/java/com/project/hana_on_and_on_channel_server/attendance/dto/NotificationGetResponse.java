package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;

import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDHMFormat;

public record NotificationGetResponse(Long notificationId, String title, String content, String date) {

    public static NotificationGetResponse fromEntity(Notification notification){
        return new NotificationGetResponse(notification.getNotificationId(), notification.getTitle(), notification.getContent(), localDateTimeToYMDHMFormat(notification.getUpdatedAt()));
    }
}
