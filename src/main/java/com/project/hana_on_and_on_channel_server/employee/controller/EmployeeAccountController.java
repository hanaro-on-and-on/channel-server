package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountRegRequest;
import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountUpsertResponse;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee/accounts")
public class EmployeeAccountController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeAccountUpsertResponse> registerEmployeeAccount(@AuthenticationPrincipal Long userId, @RequestBody EmployeeAccountRegRequest employeeAccountRegRequest){
        EmployeeAccountUpsertResponse response = employeeService.registerEmployeeAccount(userId, employeeAccountRegRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Void> updateEmployeeAccount(@AuthenticationPrincipal Long userId, @RequestBody EmployeeAccountUpsertRequest employeeAccountUpsertRequest){
        employeeService.updateEmployeeAccount(userId, employeeAccountUpsertRequest);
        return ResponseEntity.noContent().build();
    }
}
