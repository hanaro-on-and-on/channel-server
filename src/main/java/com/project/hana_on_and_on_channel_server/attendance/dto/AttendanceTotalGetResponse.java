package com.project.hana_on_and_on_channel_server.attendance.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.WorkTimeGetResponse;

import java.util.List;

public record AttendanceTotalGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCd,
        List<WorkTimeGetResponse> workTime
) {
    public static AttendanceTotalGetResponse fromEntity(EmploymentContract employmentContract, List<WorkTime> workTimeList){
        List<WorkTimeGetResponse> workTime = workTimeList.stream().map(WorkTimeGetResponse::fromEntity).toList();
        return new AttendanceTotalGetResponse(
                employmentContract.getWorkPlaceEmployee().getWorkPlaceEmployeeId(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceNm(),
                employmentContract.getWorkPlaceEmployee().getWorkPlace().getColorType().getCode(),
                workTime
        );
    }
}
