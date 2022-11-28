package com.yogit.server.report.exception;

public class AlreadyReportException extends ReportException{
    public AlreadyReportException()
    {
        super(ReportExceptionList.ALREADY_REPORT.getCODE(), ReportExceptionList.ALREADY_REPORT.getHTTPSTATUS(), ReportExceptionList.ALREADY_REPORT.getMESSAGE());
    }
}
