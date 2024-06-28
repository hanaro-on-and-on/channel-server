package com.project.hana_on_and_on_channel_server.employee.service;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeDuplicatedException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmploymentContractsRepository employmentContractsRepository;
    private final CustomWorkPlaceRepository customWorkPlaceRepository;

    @Transactional(readOnly = true)
    public WorkPlacesInvitationsListGetResponse getWorkPlacesInvitations(Long userId) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // 휴대폰 번호로, 전자서명 안 된 계약서 찾기
        List<EmploymentContract> employmentContracts = employmentContractsRepository.findByEmployeePhoneAndEmployeeSign(employee.getPhoneNumber(), false);

        List<WorkPlacesInvitationsGetResponse> workPlacesInvitationsGetResponseList = employmentContracts.stream()
                .map(contract -> new WorkPlacesInvitationsGetResponse(
                        userId,
                        contract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceName(),
                        contract.getWorkPlaceEmployee().getWorkPlace().getColorType(),
                        contract.getWorkPlaceEmployee().getWorkPlace().getOwner().getOwnerNm()))
                .collect(Collectors.toList());

        return WorkPlacesInvitationsListGetResponse.fromEntity(workPlacesInvitationsGetResponseList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomWorkPlacesCreateResponse createCustomWorkPlaces(Long userId, CustomWorkPlacesCreateRequest customWorkPlacesCreateRequest) {
        // employee 존재 여부 확인
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);

        // customWorkPlace 등록
        CustomWorkPlace customWorkPlace = customWorkPlaceRepository.save(CustomWorkPlace.builder()
                        .employee(employee)
                        .customWorkPlaceNm(customWorkPlacesCreateRequest.customWorkPlaceNm())
                        .payPerHour(customWorkPlacesCreateRequest.payPerHour())
                .build());
        return CustomWorkPlacesCreateResponse.fromEntity(customWorkPlace);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAccountUpsertResponse registerEmployeeAccount(Long userId, EmployeeAccountRegRequest employeeAccountRegRequest){
        // employee 존재 여부 확인
        employeeRepository.findByUserId(userId).ifPresent(employee -> {
            throw new EmployeeDuplicatedException();
        });

        // employee 등록
        Employee employee = employeeRepository.save(Employee.builder()
                .userId(userId)
                .accountNumber(employeeAccountRegRequest.accountNumber())
                .employeeNm(employeeAccountRegRequest.employeeNm())
                .build());
        return EmployeeAccountUpsertResponse.fromEntity(employee);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEmployeeAccount(Long userId, EmployeeAccountUpsertRequest employeeAccountUpsertRequest){
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);
        employee.registerAccountNumber(employeeAccountUpsertRequest.accountNumber());
    }
}
