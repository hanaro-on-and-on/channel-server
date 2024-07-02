package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.dto.NotificationGetResponse;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.WorkTimeGetResponse;

import java.util.List;

public record AttendanceTodayGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCd,
        List<WorkTimeGetResponse> workTime,
        List<NotificationGetResponse> notice
) {
    public static AttendanceTodayGetResponse fromEntity(EmploymentContract employmentContract, List<WorkTime> workTimeList, List<Notification> notificationList){
        List<WorkTimeGetResponse> workTime = workTimeList.stream().map(WorkTimeGetResponse::fromEntity).toList();
        List<NotificationGetResponse> notification = notificationList.stream().map(NotificationGetResponse::fromEntity).toList();
        return new AttendanceTodayGetResponse(
                employmentContract.getWorkPlaceEmployee().getWorkPlaceEmployeeId(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceNm(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getColorType().toString(),
                workTime,
                notification
        );
    }
}
