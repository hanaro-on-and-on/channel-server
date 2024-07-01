package com.project.hana_on_and_on_channel_server.paper.domain;

public record TotalHours(Long totalBasicHours, Long totalOverHours, Long totalWeeklyHolidayHours) {
    public Long calcTotalPay(Long payPerHour){
        return (long) (this.totalBasicHours * payPerHour + this.totalOverHours * payPerHour * 1.5 + this.totalWeeklyHolidayHours * payPerHour);
    }
}
