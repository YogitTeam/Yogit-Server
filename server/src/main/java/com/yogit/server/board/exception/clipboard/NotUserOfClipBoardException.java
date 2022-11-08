package com.yogit.server.board.exception.clipboard;

public class NotUserOfClipBoardException extends ClipBoardException{
    public NotUserOfClipBoardException(){
        super(ClipBoardExceptionList.NOT_USER_OF_CLIPBOARD.getCODE(), ClipBoardExceptionList.NOT_USER_OF_CLIPBOARD.getHTTPSTATUS(), ClipBoardExceptionList.NOT_USER_OF_CLIPBOARD.getMESSAGE());
    }

}
