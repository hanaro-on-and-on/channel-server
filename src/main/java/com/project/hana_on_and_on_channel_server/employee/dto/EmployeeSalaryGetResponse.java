package com.project.hana_on_and_on_channel_server.employee.dto;

public record EmployeeSalaryGetResponse(
        Boolean isConnected,
        Long id,
        Boolean isQuit,
        String workPlaceName,
        Integer payment
) {
}
