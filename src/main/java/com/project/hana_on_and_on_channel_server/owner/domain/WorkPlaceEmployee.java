package com.project.hana_on_and_on_channel_server.owner.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_place_employee")
@Entity
public class WorkPlaceEmployee extends BaseEntity {

    @Column(name = "work_place_employee_id")
    @Id @GeneratedValue
    private Long workPlaceEmployeeId;

    @Column(name = "employment_status_type_cd")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @Column(name = "color_type_cd")
    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_place_id")
    private WorkPlace workPlace;
}
