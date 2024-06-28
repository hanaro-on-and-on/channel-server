package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;

public record WorkTimeGetResponse(
        String workDayOfWeek,
        String workStartTime,
        String workEndTime,
        String restStartTime,
        String restEndTime
) {
    public static WorkTimeGetResponse fromEntity(WorkTime workTime){
        return new WorkTimeGetResponse(workTime.getWorkDayOfWeek(), workTime.getWorkStartTime().toString(),workTime.getWorkEndTime().toString(), workTime.getRestStartTime().toString(), workTime.getRestEndTime().toString());
    }
}
