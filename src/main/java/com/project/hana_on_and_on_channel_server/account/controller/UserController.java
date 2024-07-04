package com.project.hana_on_and_on_channel_server.account.controller;

import com.project.hana_on_and_on_channel_server.account.dto.AccountGetResponse;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginRequest;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginResponse;
import com.project.hana_on_and_on_channel_server.account.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final AccountServiceImpl accountService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest dto){
        UserLoginResponse response = accountService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountGetResponse>> findAccountList(@AuthenticationPrincipal Long userId){
        List<AccountGetResponse> response = accountService.findAccountList(userId);
        return ResponseEntity.ok(response);
    }
}
