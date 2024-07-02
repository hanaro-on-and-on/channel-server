package com.project.hana_on_and_on_channel_server.attendance.dto;

import java.util.List;

public record AttendanceTodayListGetResponse(List<AttendanceTodayGetResponse> works, List<AttendanceTotalGetResponse> totalWorks) {
}
