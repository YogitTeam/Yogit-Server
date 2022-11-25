package com.yogit.server.report.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateUserReportReq;
import com.yogit.server.report.dto.res.UserReportRes;
import com.yogit.server.report.entity.UserReport;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.repository.UserReportRepository;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReportServiceImpl implements UserReportService{

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<UserReportRes> createUserReport(CreateUserReportReq dto) {

        //신고하는 유저 조회
        User reportingUser = userRepository.findById(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        //신고 당하는 유저 조회
        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());

        //신고 객체 생성
        //저장
        UserReport userReport = new UserReport(dto.getContent(), reportingUser, reportedUser, dto.getReportType(), ReportStatus.ONGOIN);
        userReportRepository.save(userReport);

        //신고하는 유저 신고 한 횟수 1증가
        reportingUser.changeReportingCnt();
        //신고 당하는 유저 신고 받은 횟수 1증가
        reportedUser.changeReportedCnt();

        UserReportRes res = UserReportRes.toDto(userReport);
        return ApplicationResponse.create("유저 신고 생성 성공했습니다.", res);
    }
}
