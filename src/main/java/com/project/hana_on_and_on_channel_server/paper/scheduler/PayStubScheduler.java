package com.project.hana_on_and_on_channel_server.paper.scheduler;

import com.project.hana_on_and_on_channel_server.paper.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayStubScheduler {

    private final PaperService paperService;

    /*
    * 매일 5시 예약이체 일괄 처리
    * */
    @Scheduled(cron = "0 0 5 * * ?")  //매일 5시
    public void scheduleTransfer(){

    }


    /*
    * 매달 1일 직전 달 급여명세서 생성
    * */
    @Scheduled(cron = "0 0 5 1 * ?")  //매달 1일 5시
    public void schedulePayStubGeneration(){
        paperService.executeMonthlyPayStubGeneration();
    }
}
