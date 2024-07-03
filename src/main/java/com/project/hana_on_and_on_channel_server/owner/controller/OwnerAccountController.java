package com.project.hana_on_and_on_channel_server.owner.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/owner/accounts")
public class OwnerAccountController {

    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<OwnerAccountGetResponse> getEmployeeAccount(@AuthenticationPrincipal Long userId){
        OwnerAccountGetResponse response = ownerService.getOwnerAccount(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 사장님 Entity 생성 및 계좌 등록
     */
    @PostMapping
    public ResponseEntity<OwnerAccountUpsertResponse> registerOwnerAccount(@AuthenticationPrincipal Long userId, @RequestBody OwnerAccountUpsertRequest dto) {
        OwnerAccountUpsertResponse response = ownerService.registerOwnerAccount(userId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Void> updateOwnerAccount(@AuthenticationPrincipal Long userId, @RequestBody OwnerAccountUpsertRequest dto) {
        ownerService.updateOwnerAccount(userId, dto);
        return ResponseEntity.noContent().build();
    }
}
