package com.yogit.server.report.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ReportExceptionList {

    MAX_REPORTING_CNT("R001",BAD_REQUEST, "신고 횟수를 초과했습니다."),
    ALREADY_REPORT("R0002", BAD_REQUEST, "이미 신고한 유저입니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
