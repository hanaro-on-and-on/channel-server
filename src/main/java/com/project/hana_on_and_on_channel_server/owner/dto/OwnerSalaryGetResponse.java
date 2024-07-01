package com.project.hana_on_and_on_channel_server.owner.dto;

import java.time.LocalDate;

public record OwnerSalaryGetResponse(
        Long id,
        String employeeName,
        LocalDate workStartDate,
        Integer payment
) {
}