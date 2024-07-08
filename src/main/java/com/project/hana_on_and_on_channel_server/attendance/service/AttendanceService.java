package com.project.hana_on_and_on_channel_server.attendance.service;

import com.project.hana_on_and_on_channel_server.attendance.domain.Attendance;
import com.project.hana_on_and_on_channel_server.attendance.domain.enumType.AttendanceType;
import com.project.hana_on_and_on_channel_server.attendance.dto.*;
import com.project.hana_on_and_on_channel_server.attendance.exception.AttendanceNotFoundException;
import com.project.hana_on_and_on_channel_server.attendance.exception.GeoLocationNotFoundException;
import com.project.hana_on_and_on_channel_server.attendance.repository.AttendanceRepository;
import com.project.hana_on_and_on_channel_server.employee.exception.EmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.domain.Notification;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlace;
import com.project.hana_on_and_on_channel_server.owner.domain.WorkPlaceEmployee;
import com.project.hana_on_and_on_channel_server.attendance.dto.NotificationGetResponse;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeInvalidException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceEmployeeNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.exception.WorkPlaceNotFoundException;
import com.project.hana_on_and_on_channel_server.owner.repository.NotificationRepository;
import com.project.hana_on_and_on_channel_server.owner.repository.WorkPlaceEmployeeRepository;
import com.project.hana_on_and_on_channel_server.owner.vo.GeoPoint;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.hana_on_and_on_channel_server.paper.domain.EmploymentContract;
import com.project.hana_on_and_on_channel_server.paper.domain.WorkTime;
import com.project.hana_on_and_on_channel_server.paper.dto.WorkTimeGetResponse;
import com.project.hana_on_and_on_channel_server.paper.repository.EmploymentContractRepository;
import com.project.hana_on_and_on_channel_server.paper.repository.WorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToTodayOfWeekFormat;
import static com.project.hana_on_and_on_channel_server.common.util.LocalDateTimeUtil.localDateTimeToYMDFormat;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final WorkPlaceEmployeeRepository workPlaceEmployeeRepository;
    private final EmploymentContractRepository employmentContractRepository;
    private final WorkTimeRepository workTimeRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 출석 처리
     */
    @Transactional
    public AttendanceCheckInResponse checkIn(AttendanceCheckInRequest dto) {
        GeoPoint point = Optional.ofNullable(dto.location())
                .orElseThrow(GeoLocationNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(
                dto.workPlaceEmployeeId()).orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        Attendance attendance = attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
                        workPlaceEmployee.getWorkPlaceEmployeeId(),
                        localDateTimeToYMDFormat(LocalDateTime.now())
                )
                .orElseThrow(() -> new AttendanceNotFoundException());

        if (attendanceRepository.existsInWorkPlaceRadius(point.getLng(), point.getLat(), 500.0, dto.workPlaceEmployeeId())) {
            attendance.checkIn(LocalDateTime.now());
            return AttendanceCheckInResponse.fromEntity(attendance, true);
        }

        return AttendanceCheckInResponse.fromEntity(attendance, false);


    }


    /**
     * 퇴근 처리
     */
    @Transactional
    public AttendanceCheckOutResponse checkOut(AttendanceCheckOutRequest dto) {
        GeoPoint point = Optional.ofNullable(dto.location())
                .orElseThrow(GeoLocationNotFoundException::new);
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(
                dto.workPlaceEmployeeId()).orElseThrow(WorkPlaceEmployeeNotFoundException::new);
        Attendance attendance = attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(
                        workPlaceEmployee.getWorkPlaceEmployeeId(),
                        localDateTimeToYMDFormat(LocalDateTime.now())
                )
                .orElseThrow(() -> new AttendanceNotFoundException());;

        if (attendanceRepository.existsInWorkPlaceRadius(point.getLng(), point.getLat(), 500.0, dto.workPlaceEmployeeId())) {
            attendance.checkOut(LocalDateTime.now());
            attendance.updateAttendanceType(AttendanceType.REAL);
            return AttendanceCheckOutResponse.fromEntity(attendance, true);
        }

        return AttendanceCheckOutResponse.fromEntity(attendance, false);
    }

    public static Integer calculateDailyPayment(LocalDateTime startTime, LocalDateTime endTime, Integer restMinute, Long payPerHour) {
        LocalDateTime startZeroSecondTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), startTime.getHour(), startTime.getMinute(), 0);
        LocalDateTime endZeroSecondTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), endTime.getHour(), endTime.getMinute(), 0);
        int totalDurationMinutes = (int) Duration.between(startZeroSecondTime, endZeroSecondTime).toMinutes();

        double payment = (totalDurationMinutes - restMinute) * payPerHour / 60.0;
        return (int) Math.floor(payment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AttendanceTodayListGetResponse getTodayAttendanceList(Long userId){
        String dayOfWeek = localDateTimeToTodayOfWeekFormat(LocalDateTime.now());

        //현재 근무중인 최신 근로계약서 목록 가져오기
        List<Long> latestEmploymentContractList = attendanceRepository.findLatestEmploymentContractList(userId);
        List<EmploymentContract> employmentContractList = employmentContractRepository.findAllById(latestEmploymentContractList);

        //오늘 출결 목록
        List<AttendanceTodayGetResponse> todayList = employmentContractList.stream()
                .flatMap(employmentContract -> attendanceRepository.findByWorkPlaceEmployeeWorkPlaceEmployeeIdAndAttendDate(employmentContract.getWorkPlaceEmployee().getWorkPlaceEmployeeId(), localDateTimeToYMDFormat(LocalDateTime.now()))
                        .map(attendance -> {
                            List<Notification> notificationList = notificationRepository.findByWorkPlaceWorkPlaceId(employmentContract.getWorkPlaceEmployee().getWorkPlace().getWorkPlaceId());
                            return AttendanceTodayGetResponse.fromEntity(employmentContract, attendance, notificationList);
                        }).stream())
                .toList();

        //이외
        List<AttendanceTotalGetResponse> totalList = employmentContractList.stream()
                .filter(employmentContract -> !workTimeRepository.existsByEmploymentContractAndWorkDayOfWeek(employmentContract, dayOfWeek))
                .map(employmentContract -> {
                    List<WorkTime> workTimeList = workTimeRepository.findByEmploymentContract(employmentContract);
                    return AttendanceTotalGetResponse.fromEntity(employmentContract, workTimeList);
                })
                .toList();

        return new AttendanceTodayListGetResponse(todayList, totalList);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AttendanceWorkPlaceGetResponse getWorkPlace(Long userId, Long workPlaceEmployeeId){
        WorkPlaceEmployee workPlaceEmployee = workPlaceEmployeeRepository.findById(workPlaceEmployeeId)
                .orElseThrow(WorkPlaceEmployeeNotFoundException::new);

//        //사용자 검증
//        if(userId != workPlaceEmployee.getEmployee().getUserId()){
//            throw  new WorkPlaceEmployeeInvalidException();
//        }

        EmploymentContract employmentContract = employmentContractRepository.findFirstByWorkPlaceEmployeeOrderByCreatedAtDesc(workPlaceEmployee)
                .orElseThrow(EmployeeNotFoundException::new);
        WorkPlace workPlace = Optional.ofNullable(workPlaceEmployee.getWorkPlace()).orElseThrow(WorkPlaceNotFoundException::new);

        List<Notification> notification = notificationRepository.findByWorkPlaceOrderByCreatedAtDesc(workPlace);
        List<WorkTime> workTime = workTimeRepository.findByEmploymentContract(employmentContract);

        return new AttendanceWorkPlaceGetResponse(
                workPlaceEmployee.getWorkPlaceEmployeeId(),
                workPlace.getWorkPlaceNm(),
                workPlace.getColorType().getCode(),
                new GeoPoint((workPlace.getLocation())),
                workTime.stream().map(WorkTimeGetResponse::fromEntity).toList(),
                notification.stream().map(NotificationGetResponse::fromEntity).toList()
        );
    }
}
