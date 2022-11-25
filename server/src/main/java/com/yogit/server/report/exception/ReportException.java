package com.yogit.server.report.exception;

import com.yogit.server.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ReportException extends ApplicationException {

    protected ReportException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
