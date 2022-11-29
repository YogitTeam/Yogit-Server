package com.yogit.server.board.exception;

import static com.yogit.server.board.exception.BoardExceptionList.NOT_FOUND_USER_BOARD;

public class NotFoundUserBoard extends BoardException {
    public NotFoundUserBoard(){
        super(NOT_FOUND_USER_BOARD.getCODE(), NOT_FOUND_USER_BOARD.getHTTPSTATUS(), NOT_FOUND_USER_BOARD.getMESSAGE());
    }
}
