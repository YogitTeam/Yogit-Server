package com.yogit.server.report.service;

import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.exception.clipboard.NotFoundClipBoardException;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateClipBoardReportReq;
import com.yogit.server.report.dto.res.ClipBoardReportRes;
import com.yogit.server.report.entity.ClipBoardReport;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.exception.AlreadyReportClipBoardException;
import com.yogit.server.report.exception.MaxReportingCntException;
import com.yogit.server.report.repository.ClipBoardReportRepository;
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
public class ClipBoradReportServiceImpl implements ClipBoradReportService{

    private final ClipBoardReportRepository clipBoardReportRepository;
    private final UserRepository userRepository;
    private final ClipBoardRepository clipBoardRepository;
    private final UserService userService;


    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<ClipBoardReportRes> createClipBoardReport(CreateClipBoardReportReq dto) {

        userService.validateRefreshToken(dto.getReportingUserId(), dto.getRefreshToken());

        User reportingUser = userRepository.findByUserId(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        User reportedUser = userRepository.findByUserId(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());
        ClipBoard reportedClipBoard = clipBoardRepository.findClipBoardById(dto.getReportedClipBoardId())
                .orElseThrow(() -> new NotFoundClipBoardException());

        //validation: ???????????? ????????? ?????? ??? ?????? ??????, ???????????? ?????? 5??? ?????? ??????
        if(reportingUser.getReportingCnt() > 5){
            throw new MaxReportingCntException();
        }
        //validation: ?????? ?????? ?????? ????????? ?????? ?????? ?????? ???????????? ??????
        if(!clipBoardReportRepository.findByReportingUserIdAndReportedClipBoardId(dto.getReportingUserId(), dto.getReportedClipBoardId()).isEmpty()){
            throw new AlreadyReportClipBoardException();
        }

        ClipBoardReport clipBoardReport = new ClipBoardReport(dto.getContent(), reportingUser, reportedUser, dto.getReportType(), ReportStatus.ONGOIN, reportedClipBoard);
        clipBoardReportRepository.save(clipBoardReport);

        reportedClipBoard.changeReportedCnt();//?????? ?????? ?????? ?????? ?????? +1 ??????

        ClipBoardReportRes res = ClipBoardReportRes.toDto(clipBoardReport);
        return ApplicationResponse.create("?????? ?????? ????????? ??????????????????.",res);
    }
}
