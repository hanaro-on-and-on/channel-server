package com.project.hana_on_and_on_channel_server.employee.dto;

import java.util.List;

public record EmployeeSalaryListGetResponse(
        Integer month,
        Integer totalPayment,
        List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList
) {

    public static EmployeeSalaryListGetResponse fromEntity(
            Integer month,
            Integer totalPayment,
            List<EmployeeSalaryGetResponse> employeeSalaryGetResponseList
    ){
        return new EmployeeSalaryListGetResponse(month, totalPayment, employeeSalaryGetResponseList);
    }
}
