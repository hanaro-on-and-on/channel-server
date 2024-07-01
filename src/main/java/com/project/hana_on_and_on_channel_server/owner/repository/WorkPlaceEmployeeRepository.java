package com.project.hana_on_and_on_channel_server.owner.repository;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkPlaceEmployeeRepository extends JpaRepository<WorkPlaceEmployee, Long> {
}
