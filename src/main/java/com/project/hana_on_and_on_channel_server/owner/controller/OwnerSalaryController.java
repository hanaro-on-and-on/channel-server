package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.OwnerSalaryCalendarWorkPlaceListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerSalaryEmployeeListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerSalaryWorkPlaceListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/salaries")
public class OwnerSalaryController {

    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<OwnerSalaryWorkPlaceListGetResponse> getSalaries(@AuthenticationPrincipal Long userId, @RequestParam Integer year, @RequestParam Integer month){
        OwnerSalaryWorkPlaceListGetResponse response = ownerService.getSalaries(userId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/work-places/{workPlaceId}")
    public ResponseEntity<OwnerSalaryEmployeeListGetResponse> getWorkPlaceSalaries(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId, @RequestParam Integer year, @RequestParam Integer month){
        OwnerSalaryEmployeeListGetResponse response = ownerService.getWorkPlaceSalaries(userId, workPlaceId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<OwnerSalaryCalendarWorkPlaceListGetResponse> getCalendarSalaries(@AuthenticationPrincipal Long userId, @RequestParam Integer year, @RequestParam Integer month){
        OwnerSalaryCalendarWorkPlaceListGetResponse response = ownerService.getCalendarSalaries(userId, year, month);
        return ResponseEntity.ok(response);
    }
}
