package com.project.hana_on_and_on_channel_server.owner.service;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertRequest;
import com.project.hana_on_and_on_channel_server.owner.dto.OwnerAccountUpsertResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerDuplicatedException;
import com.project.hana_on_and_on_channel_server.owner.exception.OwnerNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOwnerAccount(Long userId, OwnerAccountUpsertRequest dto) {
        Owner owner = ownerRepository.findByUserIdAndAccountNumber(userId, dto.accountNumber()).orElseThrow(OwnerNotFoundException::new);
        owner.registerAccountNumber(dto.accountNumber());
    }
}
