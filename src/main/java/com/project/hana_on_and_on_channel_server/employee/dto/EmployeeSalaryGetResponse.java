package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.EmployeeStatus;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.paper.domain.PayStub;

public record EmployeeSalaryGetResponse(
        Boolean isConnected,
        Long id,
        Boolean isQuit,
        String workPlaceName,
        String workPlaceColorCode,
        Integer payment,
        Long payStubId
) {
    // 연결근무지
    public static EmployeeSalaryGetResponse fromEntity(WorkPlaceEmployee workPlaceEmployee, PayStub payStub, Integer payment) {
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        ColorType colorType = workPlaceEmployee.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeSalaryGetResponse(
                true,
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                workPlaceEmployee.getEmployeeStatus() == EmployeeStatus.QUIT,
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                payment,
                payStub == null ? null : payStub.getPayStubId() // payStub은 null 가능하므로, NPE 체크 X
        );
    }

    // 수동근무지
    public static EmployeeSalaryGetResponse fromEntity(CustomWorkPlace customWorkPlace, Integer payment) {
        if (customWorkPlace == null) {
            throw new CustomWorkPlaceNotFoundException();
        }
        ColorType colorType = customWorkPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeSalaryGetResponse(
                false,
                customWorkPlace.getCustomWorkPlaceId(),
                false,
                customWorkPlace.getCustomWorkPlaceNm(),
                colorType.getCode(),
                payment,
                null
        );
    }
}
