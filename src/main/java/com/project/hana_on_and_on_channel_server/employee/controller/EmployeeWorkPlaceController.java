package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee/work-places")
public class EmployeeWorkPlaceController {

    private final EmployeeService employeeService;

    @GetMapping("/invitation")
    public ResponseEntity<WorkPlacesInvitationListGetResponse> getWorkPlacesInvitations(@AuthenticationPrincipal Long userId){
        WorkPlacesInvitationListGetResponse response = employeeService.getWorkPlacesInvitations(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/custom")
    public ResponseEntity<CustomWorkPlacesCreateResponse> createCustomWorkPlaces(@AuthenticationPrincipal Long userId, @RequestBody CustomWorkPlacesCreateRequest customWorkPlacesCreateRequest){
        CustomWorkPlacesCreateResponse response = employeeService.createCustomWorkPlaces(userId, customWorkPlacesCreateRequest);
        return ResponseEntity.ok(response);
    }
}
