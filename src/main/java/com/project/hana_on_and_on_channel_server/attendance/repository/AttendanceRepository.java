package com.project.hana_on_and_on_channel_server.attendance.repository;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByWorkPlaceEmployeeAndAttendanceTypeAndAttendDateStartingWith(
            WorkPlaceEmployee workPlaceEmployee,
            String attendanceType,
            String searchMonth
    );
}
