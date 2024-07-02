package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAttendanceGetResponse;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/attendances")
public class OwnerAttendanceController {

    private final OwnerService ownerService;

    @GetMapping("/{attendanceId}")
    public ResponseEntity<OwnerAttendanceGetResponse> findAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long attendanceId){
        OwnerAttendanceGetResponse response = ownerService.findAttendance(userId, attendanceId);
        return ResponseEntity.ok(response);
    }
}
