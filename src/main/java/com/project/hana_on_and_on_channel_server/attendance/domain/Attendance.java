package com.project.hana_on_and_on_channel_server.attendance.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="attendance")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_place_employee_id")
    private WorkPlaceEmployee workPlaceEmployee;

    @Column(nullable = false)
    private String attendanceType;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime realStartTime;

    @Column(nullable = false)
    private LocalDateTime realEndTime;

    @Builder
    public Attendance(WorkPlaceEmployee workPlaceEmployee, String attendanceType, Long payPerHour,
        LocalDateTime startTime, LocalDateTime endTime, LocalDateTime realStartTime,
        LocalDateTime realEndTime) {
        this.workPlaceEmployee = workPlaceEmployee;
        this.attendanceType = attendanceType;
        this.payPerHour = payPerHour;
        this.startTime = startTime;
        this.endTime = endTime;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
    }
}
