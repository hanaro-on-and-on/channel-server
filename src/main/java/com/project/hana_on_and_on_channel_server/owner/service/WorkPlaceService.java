package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerWorkPlaceEmployeeListGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerWorkPlaceGetResponse;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkPlaceService {

    private final WorkPlaceRepository workPlaceRepository;
    private final OwnerRepository ownerRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;

    @Transactional
    public WorkPlaceUpsertResponse saveWorkPlace(WorkPlaceUpsertRequest dto) {
        Owner owner = ownerRepository.findById(dto.ownerId())
            .orElseThrow(OwnerNotFoundException::new);
        WorkPlace workPlace = WorkPlace.builder().workPlaceNm(dto.workPlaceNm())
            .address(dto.address()).location(dto.location().toPoint()).businessRegistrationNumber(dto.businessRegistrationNumber())
            .openingDate(dto.openingDate()).workPlaceStatus(dto.workPlaceStatus())
            .workPlaceType(dto.workPlaceType()).colorType(dto.colorType()).owner(owner).build();

        WorkPlace savedWorkPlace = workPlaceRepository.save(workPlace);

        return WorkPlaceUpsertResponse.fromEntity(savedWorkPlace);
    }

    @Transactional(readOnly = true)
    public OwnerWorkPlaceEmployeeListGetResponse getEmployeeList(Long userId) {
        // owner 존재 여부 확인
        Owner owner = ownerRepository.findByUserId(userId).orElseThrow(OwnerNotFoundException::new);

        List<OwnerWorkPlaceGetResponse> ownerWorkPlaceGetResponseList = new ArrayList<>();

        List<WorkPlace> workPlaceList = workPlaceRepository.findByOwnerOwnerId(owner.getOwnerId());
        for (WorkPlace workPlace : workPlaceList) {
            List<WorkPlaceEmployee> workPlaceEmployeeList = workPlaceEmployeeRepository.findByWorkPlaceWorkPlaceId(workPlace.getWorkPlaceId());
            for (WorkPlaceEmployee workPlaceEmployee : workPlaceEmployeeList) {
                ownerWorkPlaceGetResponseList.add(
                        OwnerWorkPlaceGetResponse.fromEntity(workPlaceEmployee)
                );
            }
        }
        return new OwnerWorkPlaceEmployeeListGetResponse(ownerWorkPlaceGetResponseList);
    }
}
