package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.WorkTimeGetResponse;

import java.time.LocalDateTime;
import java.util.List;

public record AttendanceTodayGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCd,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime realStartTime,
        LocalDateTime realEndTime,
        List<NotificationGetResponse> notice
) {
    public static AttendanceTodayGetResponse fromEntity(EmploymentContract employmentContract, Attendance attendance, List<Notification> notificationList) {
        List<NotificationGetResponse> notification = notificationList.stream().map(NotificationGetResponse::fromEntity).toList();
        return new AttendanceTodayGetResponse(
                employmentContract.getWorkPlaceEmployee().getWorkPlaceEmployeeId(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceNm(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getColorType().getCode(),
                attendance.getStartTime(),
                attendance.getEndTime(),
                attendance.getRealStartTime(),
                attendance.getRealEndTime(),
                notification
        );
    }
}
