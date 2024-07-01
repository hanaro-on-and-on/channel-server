package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeSalaryCalendarListGetResponse;
import com.project.hana_on_and_on_channel_server.employee.dto.EmployeeSalaryListGetResponse;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee/salaries")
public class EmployeeSalaryController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<EmployeeSalaryListGetResponse> getSalaries(@AuthenticationPrincipal Long userId, @RequestParam Integer year, @RequestParam Integer month){
        EmployeeSalaryListGetResponse response = employeeService.getSalaries(userId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<EmployeeSalaryCalendarListGetResponse> getCalendarSalaries(@AuthenticationPrincipal Long userId, @RequestParam Integer year, @RequestParam Integer month){
        EmployeeSalaryCalendarListGetResponse response = employeeService.getCalendarSalaries(userId, year, month);
        return ResponseEntity.ok(response);
    }
}
