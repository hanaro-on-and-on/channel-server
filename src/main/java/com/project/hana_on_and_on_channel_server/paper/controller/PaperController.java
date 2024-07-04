package com.project.hana_on_and_on_channel_server.paper.controller;

import com.project.hana_on_and_on_channel_server.paper.dto.PaperWorkPlaceGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.*;
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

    @PostMapping("/{workPlaceId}/employment-contracts")
    public ResponseEntity<EmploymentContractUpsertResponse> saveEmploymentContract(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceId,@RequestBody EmploymentContractUpsertRequest dto){
        EmploymentContractUpsertResponse response = paperService.saveEmploymentContract(userId, workPlaceId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employment-contracts/{employmentContractId}")
    public ResponseEntity<EmploymentContractGetResponse> findEmploymentContract(@PathVariable Long employmentContractId){
        EmploymentContractGetResponse response = paperService.findEmploymentContract(employmentContractId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employment-contracts/{employmentContractId}/e-sign")
    public ResponseEntity<EmployeeAndWorkPlaceEmployeeConnectResponse> signEmploymentContractAndConnectEmployeeToWorkPlace(@AuthenticationPrincipal Long userId, @PathVariable Long employmentContractId){
        EmployeeAndWorkPlaceEmployeeConnectResponse response = paperService.signEmploymentContractAndConnectEmployeeToWorkPlace(userId, employmentContractId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceEmployeeId}")
    public ResponseEntity<PaperWorkPlaceGetResponse> findWorkPlace(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceEmployeeId){
        PaperWorkPlaceGetResponse response = paperService.findWorkPlace(userId, workPlaceEmployeeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom/{customWorkPlaceId}")
    public ResponseEntity<PaperCustomWorkPlaceGetResponse> findCustomWorkPlace(@AuthenticationPrincipal Long userId, @PathVariable Long customWorkPlaceId){
        PaperCustomWorkPlaceGetResponse response = paperService.findCustomWorkPlace(userId, customWorkPlaceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceEmployeeId}/pay-stubs")
    public ResponseEntity<MonthlyPayStubGetResponse> getMonthlyPayStubWithAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceEmployeeId, @RequestParam int year, @RequestParam int month){
        MonthlyPayStubGetResponse response = paperService.getMonthlyPayStubWithAttendance(userId, workPlaceEmployeeId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workPlaceEmployeeId}/attendance")
    public ResponseEntity<MonthlyAttendanceGetResponse> getMonthlyAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long workPlaceEmployeeId, @RequestParam int year, @RequestParam int month){
        MonthlyAttendanceGetResponse response = paperService.getMonthlyAttendance(userId, workPlaceEmployeeId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom/{customWorkPlaceId}/attendance")
    public ResponseEntity<MonthlyAttendanceGetResponse> getMonthlyCustomAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long customWorkPlaceId, @RequestParam int year, @RequestParam int month){
        MonthlyAttendanceGetResponse response = paperService.getMonthlyCustomAttendance(userId, customWorkPlaceId, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/custom/{customWorkPlaceId}/pay-stubs")
    public ResponseEntity<MonthlyPayStubGetResponse> getMonthlyPayStubWithCustomAttendance(@AuthenticationPrincipal Long userId, @PathVariable Long customWorkPlaceId, @RequestParam int year, @RequestParam int month){
        MonthlyPayStubGetResponse response = paperService.getMonthlyPayStubWithCustomAttendance(userId, customWorkPlaceId, year, month);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pay-stubs/{payStubId}/e-sign")
    public ResponseEntity<PayStubSignResponse> signPayStub(@AuthenticationPrincipal Long userId, @PathVariable Long payStubId){
        PayStubSignResponse response = paperService.signPayStub(userId, payStubId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pay-stubs/{payStubId}/reservation")
    public ResponseEntity<SalaryTransferReserveResponse> reservePayStub(@AuthenticationPrincipal Long userId, @PathVariable Long payStubId, @RequestBody SalaryTransferReserveRequest dto){
        SalaryTransferReserveResponse response = paperService.reservePayStub(userId, payStubId, dto);
        return ResponseEntity.ok(response);
    }
}
