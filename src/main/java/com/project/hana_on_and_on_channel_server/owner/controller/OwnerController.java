package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner")
public class OwnerController {

    private final OwnerService ownerService;

    /**
     * 사장님 Entity 생성 및 계좌 등록
     */
    @PostMapping("/accounts")
    public ResponseEntity<OwnerAccountUpsertResponse> registerOwnerAccount(@AuthenticationPrincipal Long userId, @RequestBody OwnerAccountUpsertRequest dto) {
        OwnerAccountUpsertResponse response = ownerService.registerOwnerAccount(userId, dto);
        return ResponseEntity.ok(response);
    }


}
