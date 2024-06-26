package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {
}
