package com.project.hana_on_and_on_channel_server.owner.dto;

import java.time.LocalDate;

public record OwnerSalaryEmployeeGetResponse(
        Long id,
        String employeeName,
        LocalDate workStartDate,
        Integer payment
) {
}