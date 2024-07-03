package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.*;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/work-places")
public class OwnerWorkPlaceController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerWorkPlaceUpsertResponse> saveWorkPlace(@RequestBody OwnerWorkPlaceUpsertRequest dto) {
        OwnerWorkPlaceUpsertResponse response = ownerService.saveWorkPlace(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<OwnerWorkPlaceEmployeeListGetResponse> getEmployeeList(@AuthenticationPrincipal Long userId) {
        OwnerWorkPlaceEmployeeListGetResponse response = ownerService.getEmployeeList(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/quit")
    public ResponseEntity<OwnerWorkPlaceEmployeeQuitListGetResponse> getEmployeeQuitList(@AuthenticationPrincipal Long userId) {
        OwnerWorkPlaceEmployeeQuitListGetResponse response = ownerService.getEmployeeQuitList(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("employees/quit")
    public ResponseEntity<OwnerWorkPlaceEmployeeQuitResponse> quitEmployee(@AuthenticationPrincipal Long userId, @RequestBody OwnerWorkPlaceEmployeeQuitRequest dto) {
        OwnerWorkPlaceEmployeeQuitResponse response = ownerService.quitEmployee(userId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceId}/notifications")
    public ResponseEntity<OwnerNotificationListGetResponse> getNotificationList(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId) {
        OwnerNotificationListGetResponse response = ownerService.getNotificationList(userId, workPlaceId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{workPlaceId}/notifications")
    public ResponseEntity<OwnerNotificationSaveResponse> saveNotification(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId, @RequestBody OwnerNotificationSaveRequest dto) {
        OwnerNotificationSaveResponse response = ownerService.saveNotification(userId, workPlaceId, dto);
        return ResponseEntity.ok(response);
    }
}