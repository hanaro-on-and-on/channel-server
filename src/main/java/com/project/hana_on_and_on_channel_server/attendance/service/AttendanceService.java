package com.project.hana_on_and_on_channel_server.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    public static Integer calculateDailyPayment(LocalDateTime startTime, LocalDateTime endTime, Long payPerHour) {
        LocalDateTime startZeroSecondTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), startTime.getHour(), startTime.getMinute(), 0);
        LocalDateTime endZeroSecondTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), endTime.getHour(), endTime.getMinute(), 0);
        int totalDurationMinutes = (int) Duration.between(startZeroSecondTime, endZeroSecondTime).toMinutes();

        double payment = totalDurationMinutes * payPerHour / 60.0;
        return (int)Math.floor(payment);
    }
}
