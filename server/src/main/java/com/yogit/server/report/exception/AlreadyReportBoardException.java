package com.yogit.server.report.exception;

import static com.yogit.server.report.exception.ReportExceptionList.*;

public class AlreadyReportBoardException extends ReportException{
    public AlreadyReportBoardException(){
        super(ALREADY_REPORT_BOARD.getCODE(), ALREADY_REPORT_BOARD.getHTTPSTATUS(), ALREADY_REPORT_BOARD.getMESSAGE());
    }
}
