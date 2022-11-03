package com.yogit.server.board.exception.comment;


public class NotHostOfCommentException extends CommentException{
    public NotHostOfCommentException(){
        super(CommentExceptionList.NOT_FOUND_COMMENT.getCODE(), CommentExceptionList.NOT_FOUND_COMMENT.getHTTPSTATUS(), CommentExceptionList.NOT_FOUND_COMMENT.getMESSAGE());
    }

}
