package com.project.hana_on_and_on_channel_server.owner.repository;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long> {
    List<WorkPlace> findByOwnerOwnerId(Long ownerId);
}
