package com.project.hana_on_and_on_channel_server.attendance.domain;

import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceDuplicatedException;
import com.project.hana_on_and_on_channel_server.common.domain.BaseEntity;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import jakarta.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private AttendanceType attendanceType;

    @Column(nullable = false)
    private Long payPerHour;

    @Column(nullable = false)
    private String attendDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;

    @Column(nullable = false)
    private Integer restMinute;

    public void checkIn(LocalDateTime realStartTime) {
        // 이미 출석 했을 경우 예외 처리
        if(this.attendanceType == AttendanceType.REAL || this.realStartTime != null) throw new AttendanceDuplicatedException();
        this.realStartTime = realStartTime;
    }

    public void checkOut(LocalDateTime realEndTime) {
        // 이미 출석 했을 경우 예외 처리
        if(this.attendanceType == AttendanceType.REAL || this.realEndTime != null) throw new AttendanceDuplicatedException();
        this.realEndTime = realEndTime;
    }

    public void updateAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public void modifyAttendance(WorkPlaceEmployee workPlaceEmployee, AttendanceType attendanceType, Long payPerHour, String attendDate, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime realStartTime, LocalDateTime realEndTime, Integer restMinute){
        this.workPlaceEmployee = workPlaceEmployee;
        this.attendanceType = attendanceType;
        this.payPerHour = payPerHour;
        this.attendDate = attendDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
        this.restMinute = restMinute;
    }

    @Builder
    public Attendance(WorkPlaceEmployee workPlaceEmployee, AttendanceType attendanceType, Long payPerHour,
                      String attendDate,LocalDateTime startTime, LocalDateTime endTime, LocalDateTime realStartTime,
                      LocalDateTime realEndTime, Integer restMinute) {
        this.workPlaceEmployee = workPlaceEmployee;
        this.attendanceType = attendanceType;
        this.payPerHour = payPerHour;
        this.attendDate = attendDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
        this.restMinute = restMinute;
    }
}
