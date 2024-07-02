package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.OwnerWorkPlaceEmployeeListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.service.WorkPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/work-places")
public class OwnerWorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping
    public ResponseEntity<WorkPlaceUpsertResponse> saveWorkPlace(@RequestBody WorkPlaceUpsertRequest dto) {
        WorkPlaceUpsertResponse response = workPlaceService.saveWorkPlace(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<OwnerWorkPlaceEmployeeListGetResponse> getEmployeeList(@AuthenticationPrincipal Long userId) {
        OwnerWorkPlaceEmployeeListGetResponse response = workPlaceService.getEmployeeList(userId);
        return ResponseEntity.ok(response);
    }
}
