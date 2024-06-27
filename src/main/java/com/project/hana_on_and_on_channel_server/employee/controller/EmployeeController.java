package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountRegRequest;
import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/accounts")
    public ResponseEntity<Void> registerEmployeeAccount(@AuthenticationPrincipal Long userId, @RequestBody EmployeeAccountRegRequest employeeAccountRegRequest){
        employeeService.registerEmployeeAccount(userId, employeeAccountRegRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/accounts")
    public ResponseEntity<Void> updateEmployeeAccount(@AuthenticationPrincipal Long userId, @RequestBody EmployeeAccountUpsertRequest employeeAccountUpdateRequest){
        employeeService.updateEmployeeAccount(userId, employeeAccountUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
