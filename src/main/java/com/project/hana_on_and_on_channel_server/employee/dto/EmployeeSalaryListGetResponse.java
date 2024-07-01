package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record EmployeeSalaryListGetResponse(
        Integer year,
        Integer month,
        Integer totalPayment,
        List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList
) {

    public static EmployeeSalaryListGetResponse fromEntity(
            Integer year,
            Integer month,
            Integer totalPayment,
            List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList
    ){
        return new EmployeeSalaryListGetResponse(year, month, totalPayment, employeeSalaryGetResponseList);
    }
}
