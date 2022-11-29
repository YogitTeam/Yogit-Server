package com.yogit.server.report.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateClipBoardReportReq;
import com.yogit.server.report.dto.res.ClipBoardReportRes;

public interface ClipBoradReportService {

    ApplicationResponse<ClipBoardReportRes> createClipBoardReport(CreateClipBoardReportReq createClipBoardReportReq);
}
