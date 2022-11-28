package com.yogit.server.report.exception;

public class MaxReportingCntException extends ReportException{
    public MaxReportingCntException(){
        super(ReportExceptionList.MAX_REPORTING_CNT.getCODE(), ReportExceptionList.MAX_REPORTING_CNT.getHTTPSTATUS() , ReportExceptionList.MAX_REPORTING_CNT.getMESSAGE());
    }
}
