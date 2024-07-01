package com.project.hana_on_and_on_channel_server.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    private final static DateTimeFormatter ymdFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final static DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd");

    public static String localDateTimeToYMDFormat(LocalDateTime localDateTime) {
        return localDateTime.format(ymdFormat);
    }

    public static Integer localDateTimeToDayFormat(LocalDateTime localDateTime) {
        String day = localDateTime.format(dayFormat);
        return Integer.parseInt(day);
    }
}
