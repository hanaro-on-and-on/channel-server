package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.paper.domain.SalaryTransferReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalaryTransferReserveRepository extends JpaRepository<SalaryTransferReserve, Long> {

    @Query(value = "SELECT * FROM salary_transfer_reserve s WHERE s.reserve_date <= :date AND s.transfer_yn = false", nativeQuery = true)
    List<SalaryTransferReserve> findPendingTransfersBeforeOrEqualDate(String date);

}
