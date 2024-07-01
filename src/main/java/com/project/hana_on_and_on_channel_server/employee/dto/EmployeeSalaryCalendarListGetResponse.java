package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record EmployeeSalaryCalendarListGetResponse(
        Integer allCurrentPayment,
        Integer allTotalPayment,
        Integer connectedCurrentPayment,
        Integer connectedTotalPayment,
        List<EmployeeSalaryCalendarGetResponse> employeeSalaryCalendarGetResponseList
) {

    public static EmployeeSalaryCalendarListGetResponse fromEntity(
            Integer allCurrentPayment,
            Integer allTotalPayment,
            Integer connectedCurrentPayment,
            Integer connectedTotalPayment,
            List<EmployeeSalaryCalendarGetResponse> employeeSalaryCalendarGetResponseList
    ){
        return new EmployeeSalaryCalendarListGetResponse(allCurrentPayment, allTotalPayment, connectedCurrentPayment, connectedTotalPayment, employeeSalaryCalendarGetResponseList);
    }
}