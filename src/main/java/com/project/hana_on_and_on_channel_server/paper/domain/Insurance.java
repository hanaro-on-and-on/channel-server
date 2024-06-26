package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="insurance")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Insurance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceId;

    @Column(name = "insurance_name")
    private String insuranceName;

    @Column(name = "insurance_description")
    private String insuranceDescription;

    @Builder
    public Insurance(String insuranceName, String insuranceDescription) {
        this.insuranceName = insuranceName;
        this.insuranceDescription = insuranceDescription;
    }
}
