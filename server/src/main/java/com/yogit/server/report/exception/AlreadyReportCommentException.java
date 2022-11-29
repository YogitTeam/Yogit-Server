package com.yogit.server.report.exception;

public class AlreadyReportCommentException extends ReportException{
    public AlreadyReportCommentException(){
        super(ReportExceptionList.ALREADY_REPORT_COMMENT.getCODE(), ReportExceptionList.ALREADY_REPORT_COMMENT.getHTTPSTATUS(), ReportExceptionList.ALREADY_REPORT_COMMENT.getMESSAGE());
    }
}
