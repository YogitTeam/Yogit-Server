package com.yogit.server.report.service.boardreport;

import com.yogit.server.board.entity.Board;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.service.BoardService;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateBoardReportReq;
import com.yogit.server.report.dto.res.BoardReportRes;
import com.yogit.server.report.entity.BoardReport;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.exception.AlreadyReportBoardException;
import com.yogit.server.report.exception.MaxReportingCntException;
import com.yogit.server.report.repository.BoardReportRepository;
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
public class BoardReportServiceImpl implements BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardReport;
    private final UserService userService;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<BoardReportRes> createBoardReport(CreateBoardReportReq dto) {

        userService.validateRefreshToken(dto.getReportingUserId(), dto.getRefreshToken());

        User reportingUser = userRepository.findByUserId(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        User reportedUser = userRepository.findByUserId(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());
        Board reportedBoard = boardReport.findBoardById(dto.getReportedBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        //validation: ???????????? ????????? ?????? ??? ?????? ??????, ???????????? ?????? 5??? ?????? ??????
        if(reportingUser.getReportingCnt() > 5){
            throw new MaxReportingCntException();
        }
        //validation: ?????? ?????? ????????? ?????? ?????? ?????? ???????????? ??????
        if (!boardReportRepository.findByReportingUserIdAndReportedBoardId(dto.getReportingUserId(), dto.getReportedBoardId()).isEmpty()) {
            throw new AlreadyReportBoardException();
        }

        BoardReport boardReport = new BoardReport(dto.getContent(), reportingUser, reportedUser, dto.getReportType(), ReportStatus.ONGOIN, reportedBoard);
        boardReportRepository.save(boardReport);

        reportedBoard.changeReportedCnt();//????????? ?????? ?????? ?????? +1 ??????

        BoardReportRes res = BoardReportRes.toDto(boardReport);
        return ApplicationResponse.create("?????? ????????? ??????????????????.",res);
    }
}
