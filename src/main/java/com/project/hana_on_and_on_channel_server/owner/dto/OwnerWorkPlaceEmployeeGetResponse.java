package com.project.hana_on_and_on_channel_server.owner.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.Employee;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.ColorTypeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;

public record OwnerWorkPlaceEmployeeGetResponse(
        Long workPlaceEmployeeId,
        String workPlaceName,
        String colorTypeCode,
        String employeeName
) {
    public static OwnerWorkPlaceEmployeeGetResponse fromEntity(WorkPlaceEmployee workPlaceEmployee){
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        Employee employee = workPlaceEmployee.getEmployee();
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return new OwnerWorkPlaceEmployeeGetResponse(
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                employee.getEmployeeNm()
        );
    }
}
