package com.yogit.server.board.exception;

public class DuplicatedBoardUserException extends BoardException{
    public DuplicatedBoardUserException(){
        super(BoardExceptionList.DUPLICATED_BOARD_USER.getCODE(), BoardExceptionList.DUPLICATED_BOARD_USER.getHTTPSTATUS(), BoardExceptionList.DUPLICATED_BOARD_USER.getMESSAGE());
    }
}
