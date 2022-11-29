package com.yogit.server.report.exception;

public class AlreadyReportUserException extends ReportException{
    public AlreadyReportUserException()
    {
        super(ReportExceptionList.ALREADY_REPORT_USER.getCODE(), ReportExceptionList.ALREADY_REPORT_USER.getHTTPSTATUS(), ReportExceptionList.ALREADY_REPORT_USER.getMESSAGE());
    }
}
