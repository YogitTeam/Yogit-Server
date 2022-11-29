package com.yogit.server.block.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BlockExceptionList {

    ALREADY_BLOCKING("BL0001", HttpStatus.BAD_REQUEST, "이미 차단하였습니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
