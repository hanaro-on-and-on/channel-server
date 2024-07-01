package com.project.hana_on_and_on_channel_server.employee.repository;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomAttendanceMemo;
import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomAttendanceMemoRepository extends JpaRepository<CustomAttendanceMemo, Long> {
    List<CustomAttendanceMemo> findByCustomWorkPlaceAndAttendDateBetween(CustomWorkPlace customWorkPlace, String startAttendDate, String endtAttendDate);
}
