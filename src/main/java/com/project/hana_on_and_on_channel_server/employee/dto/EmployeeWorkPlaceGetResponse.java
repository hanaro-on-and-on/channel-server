package com.project.hana_on_and_on_channel_server.employee.dto;

import com.project.hana_on_and_on_channel_server.employee.domain.CustomWorkPlace;
import com.project.hana_on_and_on_channel_server.employee.exception.CustomWorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.domain.Owner;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;
import com.project.hana_on_and_on_channel_server.owner.exception.*;
import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;

public record EmployeeWorkPlaceGetResponse(
        Boolean isQuit,
        Long employeeId,
        Long employmentContractId,
        String workPlaceName,
        String colorCodeType,
        String ownerName
) {
    // 초대된 사업장
    public static EmployeeWorkPlaceGetResponse fromEntity(EmploymentContract employmentContract, Long userId){
        WorkPlaceEmployee workPlaceEmployee = employmentContract.getWorkPlaceEmployee();
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        Owner owner = workPlace.getOwner();
        if (owner == null) {
            throw new OwnerNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeWorkPlaceGetResponse(
                workPlaceEmployee.getDeletedYn(),
                userId,
                employmentContract.getEmploymentContractId(),
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                owner.getOwnerNm()
        );
    }

    // 연결된 사업장
    public static EmployeeWorkPlaceGetResponse fromEntity(WorkPlaceEmployee workPlaceEmployee, Long userId){
        if (workPlaceEmployee == null) {
            throw new WorkPlaceEmployeeNotFoundException();
        }
        WorkPlace workPlace = workPlaceEmployee.getWorkPlace();
        if (workPlace == null) {
            throw new WorkPlaceNotFoundException();
        }
        Owner owner = workPlace.getOwner();
        if (owner == null) {
            throw new OwnerNotFoundException();
        }
        ColorType colorType = workPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeWorkPlaceGetResponse(
                workPlaceEmployee.getDeletedYn(),
                userId,
                null,
                workPlace.getWorkPlaceNm(),
                colorType.getCode(),
                owner.getOwnerNm()
        );
    }

    // 수동 사업장
    public static EmployeeWorkPlaceGetResponse fromEntity(CustomWorkPlace customWorkPlace, Long userId){
        if (customWorkPlace == null) {
            throw new CustomWorkPlaceNotFoundException();
        }
        ColorType colorType = customWorkPlace.getColorType();
        if (colorType == null) {
            throw new ColorTypeNotFoundException();
        }
        return new EmployeeWorkPlaceGetResponse(
                false,
                userId,
                null,
                customWorkPlace.getCustomWorkPlaceNm(),
                colorType.getCode(),
                null
        );
    }
}
