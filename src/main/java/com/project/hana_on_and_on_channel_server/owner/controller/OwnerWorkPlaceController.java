package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.service.WorkPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/work-place")
public class OwnerWorkPlaceController {

    private final WorkPlaceService workPlaceService;

    @PostMapping
    public ResponseEntity<WorkPlaceUpsertResponse> saveWorkPlace(@RequestBody WorkPlaceUpsertRequest dto) {
        WorkPlaceUpsertResponse response = workPlaceService.saveWorkPlace(dto);
        return ResponseEntity.ok(response);
    }
}
