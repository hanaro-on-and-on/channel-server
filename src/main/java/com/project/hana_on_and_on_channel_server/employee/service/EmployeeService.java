package com.project.hana_on_and_on_channel_server.employee.service;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.dto.*;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeDuplicatedException;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.CustomWorkPlaceRepository;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
                .map(contract -> {
                    WorkPlaceEmployee workPlaceEmployee = contract.getWorkPlaceEmployee();
                    if (workPlaceEmployee == null) {
                        throw new WorkPlaceEmployeeNotFoundException();
                    }
                    WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
                    if (workPlace == null) {
                        throw new WorkPlaceNotFoundException();
                    }
                    Owner owner = workPlace.getOwner();
                    if (owner == null) {
                        throw new OwnerNotFoundException();
                    }
                    return new WorkPlacesInvitationsGetResponse(
                            userId,
                            workPlace.getWorkPlaceNm(),
                            workPlace.getColorType(),
                            owner.getOwnerNm()
                    );
                })
                .toList();

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
