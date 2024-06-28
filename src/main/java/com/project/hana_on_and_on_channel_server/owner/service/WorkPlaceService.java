package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.WorkPlaceUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkPlaceService {

    private final WorkPlaceRepository workPlaceRepository;
    private final OwnerRepository ownerRepository;

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
}
