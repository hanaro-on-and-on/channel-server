package com.project.hana_on_and_on_channel_server.account.controller;

import com.project.hana_on_and_on_channel_server.account.dto.UserLoginRequest;
import com.project.hana_on_and_on_channel_server.account.dto.UserLoginResponse;
import com.project.hana_on_and_on_channel_server.account.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final AccountServiceImpl accountServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest dto){
        UserLoginResponse response = accountServiceImpl.login(dto);
        return ResponseEntity.ok(response);
    }
}
