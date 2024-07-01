package com.project.hana_on_and_on_channel_server.employee.repository;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomWorkPlaceRepository extends JpaRepository<CustomWorkPlace, Long> {
    List<CustomWorkPlace> findByEmployee(Employee employee);
}
