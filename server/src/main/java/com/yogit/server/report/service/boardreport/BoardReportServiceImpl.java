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

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<BoardReportRes> createBoardReport(CreateBoardReportReq dto) {

        User reportingUser = userRepository.findById(dto.getReportingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new NotFoundUserException());
        Board reportedBoard = boardReport.findBoardById(dto.getReportedBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        //validation: 신고하는 유저의 신고 한 횟수 검증, 일주일에 신고 5번 이하 허용
        if(reportingUser.getReportingCnt() > 5){
            throw new MaxReportingCntException();
        }
        //validation: 신고 받는 보드가 이미 신고 받은 보드인지 검증
        if (!boardReportRepository.findByReportingUserIdAndReportedBoardId(dto.getReportingUserId(), dto.getReportedBoardId()).isEmpty()) {
            throw new AlreadyReportBoardException();
        }

        BoardReport boardReport = new BoardReport(dto.getContent(), reportingUser, reportedUser, dto.getReportType(), ReportStatus.ONGOIN, reportedBoard);
        boardReportRepository.save(boardReport);

        reportedBoard.changeReportedCnt();//게시글 신고 당한 횟수 +1 증가

        BoardReportRes res = BoardReportRes.toDto(boardReport);
        return ApplicationResponse.create("보드 신고가 생성됐습니다.",res);
    }
}
