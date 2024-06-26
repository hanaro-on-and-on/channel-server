package com.project.hana_on_and_on_channel_server.paper.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name="work_time")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class WorkTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workTimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_contract_id")
    private EmploymentContract employmentContract;

    @Column(name = "work_day_of_week")
    private String workDayOfWeek;

    @Column(name = "work_start_time")
    private LocalDateTime workStartTime;

    @Column(name = "work_end_time")
    private LocalDateTime workEndTime;

    @Column(name = "rest_start_time")
    private LocalDateTime restStartTime;

    @Column(name = "rest_end_time")
    private LocalDateTime restEndTime;

    @Builder
    public WorkTime(EmploymentContract employmentContract, String workDayOfWeek, LocalDateTime workStartTime,
                    LocalDateTime workEndTime, LocalDateTime restStartTime, LocalDateTime restEndTime) {
        this.employmentContract = employmentContract;
        this.workDayOfWeek = workDayOfWeek;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.restStartTime = restStartTime;
        this.restEndTime = restEndTime;
    }
}
