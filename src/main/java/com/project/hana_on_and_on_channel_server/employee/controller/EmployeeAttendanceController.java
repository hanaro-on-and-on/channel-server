package com.project.hana_on_and_on_channel_server.employee.controller;

import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee/attendances")
public class EmployeeAttendanceController {

    private final EmployeeService employeeService;

    @PostMapping("/memo")
    public ResponseEntity<EmployeeAttendanceCustomCreateResponse> saveCustomAttendanceMemo(@AuthenticationPrincipal Long userId, @RequestBody EmployeeAttendanceCustomCreateRequest dto){
        EmployeeAttendanceCustomCreateResponse response = employeeService.saveCustomAttendanceMemo(userId, dto);
        return ResponseEntity.ok(response);
    }
}
