package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerNotificationListGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee/work-places")
public class EmployeeWorkPlaceController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<EmployeeWorkPlaceListGetResponse> getWorkPlaces(@AuthenticationPrincipal Long userId){
        EmployeeWorkPlaceListGetResponse response = employeeService.getWorkPlaces(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom")
    public ResponseEntity<EmployeeWorkPlaceCustomListGetResponse> getCustomWorkPlaces(@AuthenticationPrincipal Long userId){
        EmployeeWorkPlaceCustomListGetResponse response = employeeService.getCustomWorkPlaces(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/custom")
    public ResponseEntity<EmployeeWorkPlaceCustomCreateResponse> createCustomWorkPlaces(@AuthenticationPrincipal Long userId, @RequestBody EmployeeWorkPlaceCustomCreateRequest employeeWorkPlaceCustomCreateRequest){
        EmployeeWorkPlaceCustomCreateResponse response = employeeService.createCustomWorkPlaces(userId, employeeWorkPlaceCustomCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceId}/notifications/recent")
    public ResponseEntity<EmployeeNotificationRecentGetResponse> getRecentNotification(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId) {
        EmployeeNotificationRecentGetResponse response = employeeService.getRecentNotification(userId, workPlaceId);
        return ResponseEntity.ok(response);
    }
}
