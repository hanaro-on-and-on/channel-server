package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;

import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToHHMMFormat;

public record WorkTimeGetResponse(
        String workDayOfWeek,
        String workStartTime,
        String workEndTime,
        String restStartTime,
        String restEndTime
) {
    public static WorkTimeGetResponse fromEntity(WorkTime workTime){
        return new WorkTimeGetResponse(workTime.getWorkDayOfWeek(), localDateTimeToHHMMFormat(workTime.getWorkStartTime()), localDateTimeToHHMMFormat(workTime.getWorkEndTime()), localDateTimeToHHMMFormat(workTime.getRestStartTime()), localDateTimeToHHMMFormat(workTime.getRestEndTime()));
    }
}
