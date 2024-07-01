package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;

public record AttendanceCheckOutRequest(Long workPlaceEmployeeId, GeoPoint location) {

}
