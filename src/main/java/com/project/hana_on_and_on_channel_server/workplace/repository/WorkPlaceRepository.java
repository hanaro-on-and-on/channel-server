package com.project.hana_on_and_on_channel_server.workplace.repository;

import com.project.hana_on_and_on_channel_server.workplace.domain.WorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long>, WorkPlaceRepositoryCustom {
}
