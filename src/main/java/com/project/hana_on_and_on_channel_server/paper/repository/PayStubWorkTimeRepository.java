package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStubWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayStubWorkTimeRepository extends JpaRepository<PayStubWorkTime, Long> {
    List<PayStubWorkTime> findByPayStub(PayStub payStub);
}
