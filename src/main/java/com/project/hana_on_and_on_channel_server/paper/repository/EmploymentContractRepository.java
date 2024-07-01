package com.project.hana_on_and_on_channel_server.paper.repository;

import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.projection.EmploymentContractSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {

    Optional<EmploymentContract> findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(WorkPlaceEmployee workPlaceEmployee);

    @Query(
            value = "SELECT EC.EMPLOYMENT_CONTRACT_ID AS employmentContractId, " +
                    "WP.COLOR_TYPE_CD AS colorTypeCd, " +
                    "WP.WORK_PLACE_NM AS workPlaceNm, " +
                    "EC.CREATED_AT AS employmentContractCreatedAt " +
                    "FROM EMPLOYEES E " +
                    "JOIN WORK_PLACE_EMPLOYEE WPE " +
                    "ON E.EMPLOYEE_ID = WPE.EMPLOYEE_ID " +
                    "JOIN WORK_PLACES WP " +
                    "ON WPE.WORK_PLACE_ID = WP.WORK_PLACE_ID " +
                    "JOIN EMPLOYMENT_CONTRACTS EC " +
                    "ON WPE.WORK_PLACE_EMPLOYEE_ID = EC.WORK_PLACE_EMPLOYEE_ID " +
                    "WHERE E.USER_ID = :userId",
            nativeQuery = true
    )
    List<EmploymentContractSummary> findEmploymentContractList(@Param("userId") Long userId);

}
