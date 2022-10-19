package com.yogit.server.board.exception.boardimage;

public class NotFoundBoardImageException extends BoardImageExeption{

    public NotFoundBoardImageException(){
        super(BoardImageExcepionList.NOT_FOUND_BOARDIMAGE.getCODE(), BoardImageExcepionList.NOT_FOUND_BOARDIMAGE.getHTTPSTATUS(), BoardImageExcepionList.NOT_FOUND_BOARDIMAGE.getMESSAGE());
    }
}
