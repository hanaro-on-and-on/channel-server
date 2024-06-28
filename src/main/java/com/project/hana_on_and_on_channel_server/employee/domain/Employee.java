package com.project.hana_on_and_on_channel_server.employee.domain;


import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="employees")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String employeeNm;

    @Column(nullable = false, length = 255)
    private String accountNumber;

    @Column(nullable = false)
    private String phoneNumber;

    public void registerAccountNumber(String accountNumber){this.accountNumber = accountNumber;}

    @Builder
    public Employee(Long userId, String employeeNm, String accountNumber) {
        this.userId = userId;
        this.employeeNm = employeeNm;
        this.accountNumber = accountNumber;
    }
}
