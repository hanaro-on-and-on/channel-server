package com.project.hana_on_and_on_channel_server.owner.dto;

import java.time.LocalDateTime;

public record OwnerAttendanceUpsertRequest(Long workPlaceEmployeeId, Long payPerHour, LocalDateTime startTime, LocalDateTime endTime, Integer restMinute) {
}
