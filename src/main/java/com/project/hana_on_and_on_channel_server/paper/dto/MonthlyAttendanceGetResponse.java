package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService.calculateDailyPayment;

public record MonthlyAttendanceGetResponse(
        Long year,
        Long month,
        Long PlaceId,
        String workPlaceNm,
        String colorTypeCd,
        Integer totalPayPerMonth,
        List<AttendanceTimeResponse> works
) {
    public static MonthlyAttendanceGetResponse fromAttendance(WorkPlace workPlace, List<Attendance> attendanceList, int year, int month){
        List<AttendanceTimeResponse> works = new ArrayList<>();
        Integer totalPayPerMonth = attendanceList.stream()
                .map(attendance -> {
                    Integer dailyPayment = calculateDailyPayment(attendance.getRealStartTime(), attendance.getRealEndTime(), attendance.getRestMinute(), attendance.getPayPerHour());
                    works.add(new AttendanceTimeResponse(attendance.getAttendDate(), attendance.getRealStartTime(), attendance.getRealEndTime(), dailyPayment));
                    return dailyPayment;
                })
                .reduce(0, Integer::sum);
        return new MonthlyAttendanceGetResponse((long)year, (long)month, workPlace.getWorkPlaceId(), workPlace.getWorkPlaceNm(), workPlace.getColorType().getCode(), totalPayPerMonth, works);
    }

    public static MonthlyAttendanceGetResponse fromCustomAttendance(CustomWorkPlace workPlace, List<CustomAttendanceMemo> attendanceList, int year, int month){
        List<AttendanceTimeResponse> works = new ArrayList<>();
        Integer totalPayPerMonth = attendanceList.stream()
                .map(attendance -> {
                    Integer dailyPayment = calculateDailyPayment(attendance.getStartTime(), attendance.getEndTime(), attendance.getRestMinute(), attendance.getPayPerHour());
                    works.add(new AttendanceTimeResponse(attendance.getAttendDate(), attendance.getStartTime(), attendance.getEndTime(), dailyPayment));
                    return dailyPayment;
                })
                .reduce(0, Integer::sum);
        return new MonthlyAttendanceGetResponse((long)year, (long)month, workPlace.getCustomWorkPlaceId(), workPlace.getCustomWorkPlaceNm(), workPlace.getColorType().getCode(), totalPayPerMonth, works);
    }

    public record AttendanceTimeResponse (String workDay, LocalDateTime startTime, LocalDateTime endTime, Integer totalPayDay){}
}
