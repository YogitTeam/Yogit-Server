package com.yogit.server.board.exception;


import static com.yogit.server.board.exception.BoardExceptionList.*;

public class NotFoundBoardException extends BoardException {
    public NotFoundBoardException(){
        super(NOT_FOUND_BOARD.getCODE(), NOT_FOUND_BOARD.getHTTPSTATUS(), NOT_FOUND_BOARD.getMESSAGE());
    }
}
