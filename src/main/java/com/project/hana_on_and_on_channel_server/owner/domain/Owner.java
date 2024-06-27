package com.project.hana_on_and_on_channel_server.owner.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "owner_nm")
    private String ownerNm;

    @Column(name = "account_number")
    private String accountNumber;

    @Builder
    public Owner(Long userId, String ownerNm, String accountNumber) {
        this.userId = userId;
        this.ownerNm = ownerNm;
        this.accountNumber = accountNumber;
    }
}
