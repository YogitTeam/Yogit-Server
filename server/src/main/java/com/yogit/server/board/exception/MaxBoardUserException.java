package com.yogit.server.board.exception;


public class MaxBoardUserException extends BoardException {
    public MaxBoardUserException(){
        super(BoardExceptionList.MAX_BOARD_USER.getCODE(), BoardExceptionList.MAX_BOARD_USER.getHTTPSTATUS(), BoardExceptionList.MAX_BOARD_USER.getMESSAGE());
    }
}
