package com.yogit.server.report.service.boardreport;

import com.yogit.server.board.service.BoardService;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateBoardReportReq;
import com.yogit.server.report.dto.res.BoardReportRes;
import com.yogit.server.report.repository.BoardReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardReportServiceImpl implements BoardReportService {

    private final BoardReportRepository boardReportRepository;

//    @Override
//    @Transactional(readOnly = false)
//    public ApplicationResponse<BoardReportRes> createBoardReport(CreateBoardReportReq createBoardReportReq) {
//
//
//    }
}
