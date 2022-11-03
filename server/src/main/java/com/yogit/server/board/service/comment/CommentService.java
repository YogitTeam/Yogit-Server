package com.yogit.server.board.service.comment;

import com.yogit.server.board.dto.request.comment.CreateCommentReq;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.global.dto.ApplicationResponse;

public interface CommentService {

    ApplicationResponse<CommentRes> createComment(CreateCommentReq createCommentReq);
}
