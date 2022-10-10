package com.yogit.server.board.exception;

public class NotHostOfBoardExcepion extends BoardException{

    public NotHostOfBoardExcepion(){
        super(BoardExceptionList.NOT_HOST_OF_BOARD.getCODE(), BoardExceptionList.NOT_HOST_OF_BOARD.getHTTPSTATUS(), BoardExceptionList.NOT_HOST_OF_BOARD.getMESSAGE());
    }
}
