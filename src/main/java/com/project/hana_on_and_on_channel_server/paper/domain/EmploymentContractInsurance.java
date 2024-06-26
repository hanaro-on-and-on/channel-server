package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="employment_contract_insurance")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class EmploymentContractInsurance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employmentContractInsuranceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_contract_id")
    private EmploymentContract employmentContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;

    @Builder
    public EmploymentContractInsurance(EmploymentContract employmentContract, Insurance insurance) {
        this.employmentContract = employmentContract;
        this.insurance = insurance;
    }
}
