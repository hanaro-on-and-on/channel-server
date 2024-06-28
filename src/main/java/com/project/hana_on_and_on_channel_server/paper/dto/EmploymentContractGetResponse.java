package com.project.hana_on_and_on_channel_server.paper.dto;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;

import java.util.List;

public record EmploymentContractGetResponse(
        Long employmentContractId,
        String workPlaceName,
        String workStartDate,
        String workSite,
        String workDetail,
        List<WorkTimeGetResponse> workTimes,
        Long payPerHour,
        Long paymentDay,
        String employeeNm,
        String employeeAddress,
        String employeePhone,
        String restDayOfWeek,
        Long bonusAmount,
        Long otherAllowancesAmount,
        String otherAllowancesName,
        Long overtimeRate
) {
    public static EmploymentContractGetResponse fromEntity(EmploymentContract employmentContract, List<WorkTime> workTimeList, String workPlaceName){

        List<WorkTimeGetResponse> workTimes = workTimeList.stream().map(WorkTimeGetResponse::fromEntity).toList();

        return new EmploymentContractGetResponse(
                employmentContract.getEmploymentContractId(),
                workPlaceName,
                employmentContract.getWorkStartDate().toString(),
                employmentContract.getWorkSite(),
                employmentContract.getWorkDetail(),
                workTimes,
                employmentContract.getPayPerHour(),
                employmentContract.getPaymentDay(),
                employmentContract.getEmployeeNm(),
                employmentContract.getEmployeeAddress(),
                employmentContract.getEmployeePhone(),
                employmentContract.getRestDayOfWeek(),
                employmentContract.getBonusAmount(),
                employmentContract.getOtherAllowancesAmount(),
                employmentContract.getOtherAllowancesName(),
                employmentContract.getOvertimeRate()
        );
    }
}
