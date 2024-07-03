package com.project.hana_on_and_on_channel_server.owner.repository;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkPlaceEmployeeRepository extends JpaRepository<WorkPlaceEmployee, Long> {
    List<WorkPlaceEmployee> findByWorkPlaceWorkPlaceId(Long workPlaceId);
    List<WorkPlaceEmployee> findByWorkPlaceWorkPlaceIdAndEmployeeStatusEquals(Long workPlaceId, EmployeeStatus employeeStatus);
    List<WorkPlaceEmployee> findByEmployee(Employee employee);
}
