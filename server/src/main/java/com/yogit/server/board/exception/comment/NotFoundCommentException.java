package com.yogit.server.board.exception.comment;

public class NotFoundCommentException extends CommentException{

    public NotFoundCommentException(){
        super(CommentExceptionList.NOT_FOUND_COMMENT.getCODE(), CommentExceptionList.NOT_FOUND_COMMENT.getHTTPSTATUS(), CommentExceptionList.NOT_FOUND_COMMENT.getMESSAGE());
    }
}
