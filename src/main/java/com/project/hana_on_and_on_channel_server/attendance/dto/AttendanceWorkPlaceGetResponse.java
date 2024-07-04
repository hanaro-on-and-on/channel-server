package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;
import com.project.hana_on_and_on_channel_server.paper.dto.WorkTimeGetResponse;

import java.util.List;

public record AttendanceWorkPlaceGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCd,
        GeoPoint location,
        List<WorkTimeGetResponse> workTime,
        List<NotificationGetResponse> notice
) {
}
