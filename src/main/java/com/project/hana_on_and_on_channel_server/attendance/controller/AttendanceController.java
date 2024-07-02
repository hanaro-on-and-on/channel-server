package com.project.hana_on_and_on_channel_server.attendance.controller;

import com.project.hana_on_and_on_channel_server.attendance.dto.*;
import com.project.hana_on_and_on_channel_server.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendances")
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

    @GetMapping("/today")
    public ResponseEntity<AttendanceTodayListGetResponse> getTodayAttendanceList(@AuthenticationPrincipal Long userId){
        AttendanceTodayListGetResponse response = attendanceService.getTodayAttendanceList(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceEmployeeId}")
    public ResponseEntity<AttendanceWorkPlaceGetResponse> getWorkPlace(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceEmployeeId){
        AttendanceWorkPlaceGetResponse response = attendanceService.getWorkPlace(userId, workPlaceEmployeeId);
        return ResponseEntity.ok(response);
    }
}
