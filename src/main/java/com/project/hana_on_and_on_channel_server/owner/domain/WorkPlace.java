package com.project.hana_on_and_on_channel_server.owner.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.WorkPlaceStatus;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.WorkPlaceType;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_places")
@Entity
public class WorkPlace extends BaseEntity {

    @Column(name = "work_place_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workPlaceId;

    @Column(name = "work_place_nm", nullable = false)
    private String workPlaceNm;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "location")
    private Point location;

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
    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Builder
    public WorkPlace(String workPlaceNm, String address, Point location, String businessRegistrationNumber, LocalDate openingDate, WorkPlaceStatus workPlaceStatus, WorkPlaceType workPlaceType, ColorType colorType, Owner owner) {
        this.workPlaceNm = workPlaceNm;
        this.address = address;
        this.location = location;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.openingDate = openingDate;
        this.workPlaceStatus = workPlaceStatus;
        this.workPlaceType = workPlaceType;
        this.colorType = colorType;
        this.owner = owner;
    }
}
