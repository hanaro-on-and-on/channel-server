package com.project.hana_on_and_on_channel_server.attendance.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckInRequest;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckInResponse;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckOutRequest;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckOutResponse;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.attendance.exception.GeoLocationNotFoundException;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;

    /**
     * 출석 처리
     */
    @Transactional
    public AttendanceCheckInResponse checkIn(AttendanceCheckInRequest dto) {
        GeoPoint point = Optional.ofNullable(dto.location())
                .orElseThrow(GeoLocationNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(
                dto.workPlaceEmployeeId()).orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        Attendance attendance = attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
                        workPlaceEmployee.getWorkPlaceEmployeeId(),
                        LocalDateTimeUtil.localDateTimeToYMDFormat(LocalDateTime.now())
                )
                .orElseThrow(() -> new AttendanceNotFoundException());

        if (attendanceRepository.existsInWorkPlaceRadius(point.getLng(), point.getLat(), 500.0, dto.workPlaceEmployeeId())) {
            attendance.checkIn(LocalDateTime.now());
        }

        return AttendanceCheckInResponse.fromEntity(attendance);
    }


    /**
     * 퇴근 처리
     */
    @Transactional
    public AttendanceCheckOutResponse checkOut(AttendanceCheckOutRequest dto) {
        GeoPoint point = Optional.ofNullable(dto.location())
                .orElseThrow(GeoLocationNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(
                dto.workPlaceEmployeeId()).orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        Attendance attendance = attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
                        workPlaceEmployee.getWorkPlaceEmployeeId(),
                        LocalDateTimeUtil.localDateTimeToYMDFormat(LocalDateTime.now())
                )
                .orElseThrow(() -> new AttendanceNotFoundException());;

        if (attendanceRepository.existsInWorkPlaceRadius(point.getLng(), point.getLat(), 500.0, dto.workPlaceEmployeeId())) {
            attendance.checkOut(LocalDateTime.now());
            attendance.updateAttendanceType(AttendanceType.REAL);
        }

        return AttendanceCheckOutResponse.fromEntity(attendance);
    }

    public static Integer calculateDailyPayment(LocalDateTime startTime, LocalDateTime endTime, Integer restMinute, Long payPerHour) {
        LocalDateTime startZeroSecondTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), startTime.getHour(), startTime.getMinute(), 0);
        LocalDateTime endZeroSecondTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), endTime.getHour(), endTime.getMinute(), 0);
        int totalDurationMinutes = (int) Duration.between(startZeroSecondTime, endZeroSecondTime).toMinutes();

        double payment = (totalDurationMinutes - restMinute) * payPerHour / 60.0;
        return (int) Math.floor(payment);
    }
}
