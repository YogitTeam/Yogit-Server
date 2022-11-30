package com.yogit.server.utils.scheduler.report;

import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportSchedulerServiceImpl implements ReportSchedulerService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public void resetUserReportingCnt() {
        userRepository.resetUserReportingCnt();
    }
}
