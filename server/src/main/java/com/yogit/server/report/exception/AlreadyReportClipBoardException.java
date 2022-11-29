package com.yogit.server.report.exception;

import static com.yogit.server.report.exception.ReportExceptionList.*;

public class AlreadyReportClipBoardException extends ReportException{
    public AlreadyReportClipBoardException(){
        super(ALREADY_REPORT_CLIPBOARD.getCODE(), ALREADY_REPORT_CLIPBOARD.getHTTPSTATUS(), ALREADY_REPORT_CLIPBOARD.getMESSAGE());
    }
}
