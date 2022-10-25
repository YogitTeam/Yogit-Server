package com.yogit.server.board.exception.clipboard;

public class NotFoundClipBoardException extends ClipBoardException{

    public NotFoundClipBoardException(){
        super(ClipBoardExceptionList.NOT_FOUND_CLIP_BOARD.getCODE(), ClipBoardExceptionList.NOT_FOUND_CLIP_BOARD.getHTTPSTATUS(), ClipBoardExceptionList.NOT_FOUND_CLIP_BOARD.getMESSAGE());
    }
}
