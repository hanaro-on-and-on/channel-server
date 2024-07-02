package com.project.hana_on_and_on_channel_server.attendance.repository;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query(
            value =
                    "SELECT EXISTS ("
                            + "    SELECT 1"
                            + "    FROM work_places wp"
                            + "    CROSS JOIN (SELECT CAST(ST_SetSRID(ST_Point(:longitude, :latitude), 4326) AS geography) AS x) AS T "
                            + "    WHERE ST_DWithin(CAST(wp.location AS geography), T.x, :radius) "
                            + "    AND wp.work_place_id = :workPlaceId "
                            + ") AS result;",
            nativeQuery = true
    )
    Boolean existsInWorkPlaceRadius(Double longitude, Double latitude, Double radius, Long workPlaceId);

    Attendance findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
            Long workPlaceEmployeeId,
            String attendDate
    );

    List<Attendance> findByWorkPlaceEmployeeAndAttendDateStartingWith(
            WorkPlaceEmployee workPlaceEmployee,
            String searchDate
    );
    List<Attendance> findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
            WorkPlaceEmployee workPlaceEmployee,
            AttendanceType attendanceType,
            String searchDate
    );

    @Query(value = "SELECT employmentContractId FROM (" +
            "    SELECT EC.EMPLOYMENT_CONTRACT_ID AS employmentContractId, " +
            "        ROW_NUMBER() OVER (PARTITION BY EC.WORK_PLACE_EMPLOYEE_ID ORDER BY EC.created_at DESC) AS rn " +
            "    FROM EMPLOYMENT_CONTRACTS EC " +
            "    JOIN WORK_PLACE_EMPLOYEE WPE ON EC.WORK_PLACE_EMPLOYEE_ID = WPE.WORK_PLACE_EMPLOYEE_ID " +
            "    JOIN EMPLOYEES E ON WPE.EMPLOYEE_ID = E.EMPLOYEE_ID " +
            "    WHERE E.USER_ID = :userId " +
            "    AND WPE.RESIGNED_YN = FALSE " +
            ") subquery " +
            "WHERE rn = 1", nativeQuery = true)
    List<Long> findLatestEmploymentContractList(Long userId);

}
