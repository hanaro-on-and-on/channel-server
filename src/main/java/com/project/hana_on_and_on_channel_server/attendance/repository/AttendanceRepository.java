package com.project.hana_on_and_on_channel_server.attendance.repository;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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

    Optional<Attendance> findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
            Long workPlaceEmployeeId,
            String attendDate
    );

    List<Attendance> findByWorkPlaceEmployeeAndAttendDateStartingWith(
            WorkPlaceEmployee workPlaceEmployee,
            String searchDate
    );
    List<Attendance> findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
            WorkPlaceEmployee workPlaceEmployee,
            String attendanceType,
            String searchDate
    );
}
