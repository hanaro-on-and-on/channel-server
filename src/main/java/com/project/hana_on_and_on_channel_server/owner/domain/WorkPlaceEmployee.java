package com.project.hana_on_and_on_channel_server.owner.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_place_employee")
@Entity
public class WorkPlaceEmployee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workPlaceEmployeeId;

    @Column(name = "employment_status_type_cd")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @Column(name = "color_type_cd")
    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    @Column(name = "work_start_date")
    private LocalDate workStartDate;

    @Column(name = "work_end_date")
    private LocalDate workEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_place_id")
    private WorkPlace workPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private Boolean resignedYn;

    public void registerEmployee(Employee employee){
        this.employee=employee;
    }
  
    public Boolean quitEmployee(){
        if (this.employeeStatus == EmployeeStatus.WORKING) {
            this.employeeStatus = EmployeeStatus.QUIT;
            return true;
        }
        return false;

    @Builder
    public WorkPlaceEmployee(EmployeeStatus employeeStatus, ColorType colorType, LocalDate workStartDate, WorkPlace workPlace) {
        this.employeeStatus = employeeStatus;
        this.colorType = colorType;
        this.workStartDate = workStartDate;
        this.workPlace = workPlace;
        this.resignedYn = Boolean.FALSE;
    }
}
