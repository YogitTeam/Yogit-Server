package com.yogit.server.report.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateUserReportReq;
import com.yogit.server.report.dto.res.UserReportRes;
import com.yogit.server.report.entity.UserReport;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.exception.AlreadyReportUserException;
import com.yogit.server.report.exception.MaxReportingCntException;
import com.yogit.server.report.repository.UserReportRepository;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReportServiceImpl implements UserReportService{

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<UserReportRes> createUserReport(CreateUserReportReq dto) {

        userService.validateRefreshToken(dto.getReportingUserId(), dto.getRefreshToken());

        User reportingUser = userRepository.findByUserId(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        User reportedUser = userRepository.findByUserId(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());

        //validation: 신고하는 유저의 신고 한 횟수 검증, 일주일에 신고 5번 이하 허용
        if(reportingUser.getReportingCnt() > 5){
            throw new MaxReportingCntException();
        }
        //validation: 신고 받는 유저가 이미 신고 받은 유저인지 검증
        if(!userReportRepository.findByReportingUserIdAndReportedUserId(dto.getReportingUserId(), dto.getReportedUserId()).isEmpty()){
            throw new AlreadyReportUserException();
        }

        //신고 객체 생성
        UserReport userReport = new UserReport(dto.getContent(), reportingUser, reportedUser, dto.getReportTypeNum(), ReportStatus.ONGOIN);
        userReportRepository.save(userReport);

        reportingUser.changeReportingCnt();  //신고하는 유저 신고 한 횟수 1증가
        reportedUser.changeReportedCnt(); //신고 당하는 유저 신고 받은 횟수 1증가

        UserReportRes res = UserReportRes.toDto(userReport);
        return ApplicationResponse.create("유저 신고 생성 성공했습니다.", res);
    }
}
