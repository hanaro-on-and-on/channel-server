package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAttendanceGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAttendanceUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAttendanceUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/manual")
    public ResponseEntity<OwnerAttendanceUpsertResponse> saveAttendance(@AuthenticationPrincipal Long userId, @RequestBody OwnerAttendanceUpsertRequest dto){
        OwnerAttendanceUpsertResponse response = ownerService.saveAttendance(userId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/manual/{attendanceId}")
    public ResponseEntity<OwnerAttendanceUpsertResponse> updateAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long attendanceId, @RequestBody OwnerAttendanceUpsertRequest dto){
        OwnerAttendanceUpsertResponse response = ownerService.updateAttendance(userId, attendanceId, dto);
        return ResponseEntity.ok(response);
    }
}
