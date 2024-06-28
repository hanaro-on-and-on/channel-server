package com.project.hana_on_and_on_channel_server.paper.service;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractGetResponse;
import com.project.hana_on_and_on_channel_server.paper.dto.EmploymentContractListGetResponse;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmploymentContractListGetResponse> findEmploymentContractList (Long userId){
        List<EmploymentContractSummary> employmentContractList = employmentContractRepository.findEmploymentContractList(userId);

        return employmentContractList.stream().map(EmploymentContractListGetResponse::fromProjection).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmploymentContractGetResponse findEmploymentContract(Long employmentContractId){
        EmploymentContract employmentContract = employmentContractRepository.findById(employmentContractId).orElseThrow(EmploymentContractNotFoundException::new);
        List<WorkTime> workTimeList = workTimeRepository.findByEmploymentContractEmploymentContractId(employmentContractId);
        String workPlaceName = employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceName();

        return EmploymentContractGetResponse.fromEntity(employmentContract, workTimeList, workPlaceName);
    }
}
