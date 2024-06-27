package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerDuplicatedException;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerAccountUpsertResponse registerOwnerAccount(Long userId, OwnerAccountUpsertRequest dto) {
        ownerRepository.findByUserIdAndAccountNumber(userId, dto.accountNumber()).ifPresent(owner -> {
            throw new OwnerDuplicatedException();
        });
        Owner owner = ownerRepository.save(Owner.builder()
            .userId(userId)
            .accountNumber(dto.accountNumber())
            .ownerNm(dto.ownerNm())
            .build());
        return OwnerAccountUpsertResponse.fromEntity(owner);
    }
}
