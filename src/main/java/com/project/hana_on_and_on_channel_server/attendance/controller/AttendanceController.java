package com.project.hana_on_and_on_channel_server.attendance.controller;

import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckInRequest;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckInResponse;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckOutRequest;
import com.project.hana_on_and_on_channel_server.attendance.dto.AttendanceCheckOutResponse;
import com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceCheckInResponse> saveWorkPlace(@RequestBody AttendanceCheckInRequest dto) {
        AttendanceCheckInResponse response = attendanceService.checkIn(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceCheckOutResponse> saveWorkPlace(@RequestBody AttendanceCheckOutRequest dto) {
        AttendanceCheckOutResponse response = attendanceService.checkOut(dto);
        return ResponseEntity.ok(response);
    }
}
