package com.yogit.server.report.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateUserReportReq;
import com.yogit.server.report.dto.res.UserReportRes;

public interface UserReportService{

    ApplicationResponse<UserReportRes> createUserReport(CreateUserReportReq dto);
}
