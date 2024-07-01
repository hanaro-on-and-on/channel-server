package com.project.hana_on_and_on_channel_server.paper.controller;

import com.project.hana_on_and_on_channel_server.paper.dto.EmployeeAndWorkPlaceEmployeeConnectResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractListGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.MonthlyPayStubGetResponse;
import com.project.hana_on_and_on_channel_server.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/papers")
public class PaperController {

    private final PaperService paperService;

    @GetMapping
    public ResponseEntity<List<EmploymentContractListGetResponse>> findEmploymentContractList(@AuthenticationPrincipal Long userId){
        List<EmploymentContractListGetResponse> response = paperService.findEmploymentContractList(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employment-contracts/{employmentContractId}")
    public ResponseEntity<EmploymentContractGetResponse> findEmploymentContract(@PathVariable Long employmentContractId){
        EmploymentContractGetResponse response = paperService.findEmploymentContract(employmentContractId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employment-contracts/{employmentContractId}/e-sign")
    public ResponseEntity<EmployeeAndWorkPlaceEmployeeConnectResponse> signEmploymentContractAndConnectEmployeeToWorkPlace(@AuthenticationPrincipal Long userId, @PathVariable Long employmentContractId){
        EmployeeAndWorkPlaceEmployeeConnectResponse response = paperService.signEmploymentContractAndConnectEmployeeToWorkPlace(userId, employmentContractId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceEmployeeId}/pay-stubs?year={year}&month={month}")
    public ResponseEntity<MonthlyPayStubGetResponse> getMonthlyPayStubWithAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceEmployeeId, @RequestParam int year, @RequestParam int month){
        MonthlyPayStubGetResponse response = paperService.getMonthlyPayStubWithAttendance(userId, workPlaceEmployeeId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/papers/{customWorkPlaceId}/pay-stubs?year={year}&month={month}")
    public ResponseEntity<MonthlyPayStubGetResponse> getMonthlyPayStubWithCustomAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long customWorkPlaceId, @RequestParam int year, @RequestParam int month){
        MonthlyPayStubGetResponse response = paperService.getMonthlyPayStubWithCustomAttendance(userId, customWorkPlaceId, year, month);
        return ResponseEntity.ok(response);
    }
}
