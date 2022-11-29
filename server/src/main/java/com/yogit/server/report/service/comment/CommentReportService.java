package com.yogit.server.report.service.comment;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateCommentReportReq;
import com.yogit.server.report.dto.res.CommentReportRes;

public interface CommentReportService {

    ApplicationResponse<CommentReportRes> createCommentReport(CreateCommentReportReq createCommentReportReq);
}
