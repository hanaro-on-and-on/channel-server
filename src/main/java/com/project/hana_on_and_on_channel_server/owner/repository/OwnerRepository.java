package com.project.hana_on_and_on_channel_server.owner.repository;

import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByUserIdAndAccountNumber(Long userId, String accountNumber);
}
