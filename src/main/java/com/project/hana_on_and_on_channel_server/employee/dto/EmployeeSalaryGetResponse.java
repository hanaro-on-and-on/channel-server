package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

public record EmployeeSalaryGetResponse(
        Boolean isConnected,
        Long id,
        Boolean isQuit,
        String workPlaceName,
        ColorType workPlaceColor,
        Integer payment
) {
}
