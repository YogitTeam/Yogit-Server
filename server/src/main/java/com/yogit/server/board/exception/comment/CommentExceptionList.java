package com.yogit.server.board.exception.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionList {

    NOT_FOUND_COMMENT("CM0001", NOT_FOUND, "존재하지 않는 Comment입니다."),
    NOT_HOST_OF_COMMENT("CM0002", HttpStatus.BAD_REQUEST, "요청한 유저가 코멘트의 호스트가 아닙니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
