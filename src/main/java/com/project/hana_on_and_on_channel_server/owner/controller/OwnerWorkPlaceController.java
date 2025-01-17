package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.dto.*;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/work-places")
public class OwnerWorkPlaceController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerWorkPlaceUpsertResponse> saveWorkPlace(@AuthenticationPrincipal Long userId, @RequestBody OwnerWorkPlaceUpsertRequest dto) {
        OwnerWorkPlaceUpsertResponse response = ownerService.saveWorkPlace(userId, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/valid/registration-number")
    public ResponseEntity<OwnerWorkPlaceCheckRegistrationNumberResponse> checkRegistrationNumber(@AuthenticationPrincipal Long userId, @RequestBody OwnerWorkPlaceCheckRegistrationNumberRequest dto) {
        OwnerWorkPlaceCheckRegistrationNumberResponse response = ownerService.checkRegistrationNumber(userId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<OwnerWorkPlaceEmployeeListGetResponse> getEmployeeList(@AuthenticationPrincipal Long userId, EmployeeStatus employeeStatus) {
        OwnerWorkPlaceEmployeeListGetResponse response = ownerService.getEmployeeList(userId, employeeStatus);
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

    @PutMapping("/{workPlaceId}/notifications/{notificationId}")
    public ResponseEntity<OwnerNotificationEditResponse> editNotification(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId, @PathVariable Long notificationId, @RequestBody OwnerNotificationEditRequest dto) {
        OwnerNotificationEditResponse response = ownerService.editNotification(userId, workPlaceId, notificationId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{workPlaceId}/notifications/{notificationId}")
    public ResponseEntity<OwnerNotificationRemoveResponse> removeNotification(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId, @PathVariable Long notificationId) {
        OwnerNotificationRemoveResponse response = ownerService.removeNotification(userId, workPlaceId, notificationId);
        return ResponseEntity.ok(response);
    }
}