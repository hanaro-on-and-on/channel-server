package com.project.hana_on_and_on_channel_server.owner.dto;

public record OwnerNotificationSaveRequest(
        String title,
        String content
) {
}