package com.yogit.server.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum BoardExceptionList {

    NOT_FOUND_BOARD("B0001", NOT_FOUND,"존재하지 않는 Board아이디입니다."),
    NOT_HOST_OF_BOARD("B0002",HttpStatus.BAD_REQUEST, "요청한 유저가 호스트가 아닙니다."),
    MAX_BOARD_USER("B003", HttpStatus.BAD_REQUEST, "보드 인원이 다 찼습니다."),
    DUPLICATED_BOARD_USER("B004", HttpStatus.BAD_REQUEST, "이미 보드에 참여했습니다."),
    NOT_FOUND_USER_BOARD("B005", NOT_FOUND, "보드에 가입된 인원이 아닙니다."),
    INVALID_MYCLUBTYPE("B006", BAD_REQUEST, "유효하지 않은 myClubType입니다.");

    private final String CODE;
    private final HttpStatus HTTPSTATUS;
    private final String MESSAGE;
}
