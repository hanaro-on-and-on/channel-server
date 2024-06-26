package com.project.hana_on_and_on_channel_server.workplace.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.workplace.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.workplace.domain.enumType.WorkPlaceStatus;
import com.project.hana_on_and_on_channel_server.workplace.domain.enumType.WorkPlaceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_places")
@Entity
public class WorkPlace extends BaseEntity {

    @Column(name = "work_place_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workPlaceId;

    @Column(name = "work_place_nm", nullable = false)
    private String workPlaceName;

    // Todo: Change Type for Geometry
    @Column(name = "location")
    private String location;

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    // Todo: Choose 'LocalDateTime' or 'LocalDate'
    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "work_place_status")
    @Enumerated(EnumType.STRING)
    private WorkPlaceStatus workPlaceStatus;

    @Column(name = "work_place_type")
    @Enumerated(EnumType.STRING)
    private WorkPlaceType workPlaceType;

    @Column(name = "color_type_cd")
    private ColorType colorType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "workPlace")
    List<WorkPlaceEmployee> workPlaceEmployees = new ArrayList<>();

    @OneToMany(mappedBy = "workPlace")
    List<Notification> notifications = new ArrayList<>();

}
