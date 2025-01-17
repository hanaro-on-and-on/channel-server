package com.project.hana_on_and_on_channel_server.employee.domain;

import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
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


@Entity(name="custom_attendance_memos")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CustomAttendanceMemo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customAttendanceMemoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_work_place_id")
    private CustomWorkPlace customWorkPlace;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(nullable = false)
    private String attendDate;  // yyyymmdd

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private Integer restMinute;

    @Builder
    public CustomAttendanceMemo(CustomWorkPlace customWorkPlace, Long payPerHour, String attendDate, LocalDateTime startTime, LocalDateTime endTime, Integer restMinute) {
        this.customWorkPlace = customWorkPlace;
        this.payPerHour = payPerHour;
        this.attendDate = attendDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.restMinute = restMinute;
    }
}
