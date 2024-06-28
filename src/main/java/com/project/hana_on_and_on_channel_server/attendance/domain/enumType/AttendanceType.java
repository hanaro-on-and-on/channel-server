package com.project.hana_on_and_on_channel_server.attendance.domain.enumType;

import lombok.Getter;

@Getter
public enum AttendanceType {

    EXPECT("EXPECT"), REAL("REAL");

    private String code;

    AttendanceType(String code) {
        this.code = code;
    }
}
