package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmploymentContractsRepository extends JpaRepository<EmploymentContract, Long> {
    List<EmploymentContract> findByEmployeePhoneAndEmployeeSign(String employeePhone, Boolean employeeSign);
}
