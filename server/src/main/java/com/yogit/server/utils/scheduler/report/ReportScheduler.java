package com.yogit.server.utils.scheduler.report;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ReportSchedulerService reportSchedulerService;

    //하루 최대 신고 회수 5회 초기화
    @Scheduled(cron = "0 0 0 * * *")
    public void resetPerDayUserReportingCnt(){
        reportSchedulerService.resetUserReportingCnt();
    }
}
