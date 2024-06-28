package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.employee.repository.EmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.EmployeeAndWorkPlaceEmployeeConnectResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractListGetResponse;
import com.project.hana_on_and_on_channel_server.paper.exception.EmployeeContractAlreadyConnectedException;
import com.project.hana_on_and_on_channel_server.paper.exception.EmploymentContractNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final EmploymentContractRepository employmentContractRepository;
    private final WorkTimeRepository workTimeRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmploymentContractListGetResponse> findEmploymentContractList (Long userId){
        List<EmploymentContractSummary> employmentContractList = employmentContractRepository.findEmploymentContractList(userId);

        return employmentContractList.stream().map(EmploymentContractListGetResponse::fromProjection).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmploymentContractGetResponse findEmploymentContract(Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);
        List<WorkTime> workTimeList = workTimeRepository.findByEmploymentContractEmploymentContractId(employmentContractId);
        String workPlaceName = employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceNm();

        return EmploymentContractGetResponse.fromEntity(employmentContract, workTimeList, workPlaceName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmployeeAndWorkPlaceEmployeeConnectResponse signEmploymentContractAndConnectEmployeeToWorkPlace(Long userId, Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);

        // 근로계약서 서명되어있을 경우 예외처리
        if(employmentContract.getEmployeeSign()){
            throw new EmployeeContractAlreadyConnectedException();
        }

        // 근로계약서 서명
        employmentContract.registerEmployeeSign(Boolean.TRUE);

        // 직원 연동
        Employee employee = employeeRepository.findByUserId(userId).orElseThrow(EmployeeNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = employmentContract.getWorkPlaceEmployee();
        workPlaceEmployee.registerEmployee(employee);

        return new EmployeeAndWorkPlaceEmployeeConnectResponse(workPlaceEmployee.getWorkPlaceEmployeeId());
    }
}
