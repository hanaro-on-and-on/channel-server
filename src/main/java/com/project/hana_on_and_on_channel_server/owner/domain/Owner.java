package com.project.hana_on_and_on_channel_server.owner.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.workplace.domain.WorkPlace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "owners")
@Entity
public class Owner extends BaseEntity {

    @Column(name = "owner_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;

    @Column(name = "owner_nm")
    private String ownerName;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<WorkPlace> workPlaces = new ArrayList<>();

    @Builder
    private Owner(Long ownerId, String ownerName, String accountNumber) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.accountNumber = accountNumber;
    }


}
