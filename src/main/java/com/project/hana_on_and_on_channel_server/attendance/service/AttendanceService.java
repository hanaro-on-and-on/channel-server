package com.project.hana_on_and_on_channel_server.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    public static Integer calculateDailyPayment(LocalDateTime startTime, LocalDateTime endTime, Long payPerHour) {
        int startMinutes = startTime.getMinute();
        int endMinutes = endTime.getMinute();
        int durationMinutes = endMinutes - startMinutes;

        double payment = durationMinutes * payPerHour / 60.0;
        return (int)Math.floor(payment);
    }
}
