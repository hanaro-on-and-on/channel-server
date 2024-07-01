package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PayStubRepository extends JpaRepository<PayStub, Long> {

    @Query(value = "SELECT * FROM pay_stubs p WHERE p.work_place_employee_id = :workPlaceEmployeeId AND EXTRACT(YEAR FROM p.created_at) = :year AND EXTRACT(MONTH FROM p.created_at) = :month LIMIT 1", nativeQuery = true)
    Optional<PayStub> findByWorkPlaceEmployeeIdAndYearAndMonth(Long workPlaceEmployeeId, int year, int month);
}
