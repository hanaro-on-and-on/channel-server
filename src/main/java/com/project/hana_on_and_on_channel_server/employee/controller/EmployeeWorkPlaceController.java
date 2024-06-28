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
    public ResponseEntity<EmployeeWorkPlaceInvitationListGetResponse> getWorkPlacesInvitations(@AuthenticationPrincipal Long userId){
        EmployeeWorkPlaceInvitationListGetResponse response = employeeService.getWorkPlacesInvitations(userId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/custom")
    public ResponseEntity<EmployeeWorkPlaceCustomCreateResponse> createCustomWorkPlaces(@AuthenticationPrincipal Long userId, @RequestBody EmployeeWorkPlaceCustomCreateRequest employeeWorkPlaceCustomCreateRequest){
        EmployeeWorkPlaceCustomCreateResponse response = employeeService.createCustomWorkPlaces(userId, employeeWorkPlaceCustomCreateRequest);
        return ResponseEntity.ok(response);
    }
}
