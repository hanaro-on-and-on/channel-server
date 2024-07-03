package com.project.hana_on_and_on_channel_server.employee.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="custom_work_places")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CustomWorkPlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customWorkPlaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String customWorkPlaceNm;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(name = "color_type_cd")
    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    @Builder
    public CustomWorkPlace(Employee employee, String customWorkPlaceNm, Long payPerHour) {
        this.employee = employee;
        this.customWorkPlaceNm = customWorkPlaceNm;
        this.payPerHour = payPerHour;
    }
}
