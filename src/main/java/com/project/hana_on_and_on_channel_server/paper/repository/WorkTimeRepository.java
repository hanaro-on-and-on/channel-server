package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {
    List<WorkTime> findByEmploymentContractEmploymentContractId(Long employmentContractId);
    boolean existsByEmploymentContractAndWorkDayOfWeek(EmploymentContract employmentContract, String dayOfWeek);
    List<WorkTime> findByEmploymentContract(EmploymentContract employmentContract);
}
