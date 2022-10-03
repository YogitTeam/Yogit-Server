package com.yogit.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionList {

    FAIL_UPLOAD_IMAGE("G0001", INTERNAL_SERVER_ERROR,"이미지 업로드에 실패했습니다 잠시 후 다시 시도해주세요.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
