package com.project.hana_on_and_on_channel_server.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeUtil {

    private final static DateTimeFormatter ymdFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final static DateTimeFormatter ymdDashFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd");
    private final static DateTimeFormatter dayOfWeekFormat = DateTimeFormatter.ofPattern("EEEE", Locale.KOREAN);
    private final static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    private final static DateTimeFormatter ymdHmFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String localDateTimeToYMDFormat(LocalDateTime localDateTime) {
        return localDateTime.format(ymdFormat);
    }

    public static String localDateTimeToYMDDashFormat(LocalDateTime localDateTime) {
        return localDateTime.format(ymdDashFormat);
    }

    public static String localDateTimeToTodayOfWeekFormat(LocalDateTime localDateTime) {
        return localDateTime.format(dayOfWeekFormat);
    }

    public static String localDateTimeToHHMMFormat(LocalDateTime localDateTime){
        return localDateTime.format(timeFormat);
    }

    public static String localDateTimeToYMDHMFormat(LocalDateTime localDateTime){
        return localDateTime.format(ymdHmFormat);
    }

    public static Integer localDateTimeToDayFormat(LocalDateTime localDateTime) {
        String day = localDateTime.format(dayFormat);
        return Integer.parseInt(day);
    }
}
