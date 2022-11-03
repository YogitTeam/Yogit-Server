package com.yogit.server.board.controller;

import com.yogit.server.board.dto.request.comment.CreateCommentReq;
import com.yogit.server.board.dto.request.comment.DeleteCommentReq;
import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.dto.response.comment.DeleteCommentRes;
import com.yogit.server.board.service.comment.CommentService;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.Gender;
import com.yogit.server.user.entity.Nationality;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/comments")
public class CommenrController {

    private final CommentService commentService;
    /**
     * 코멘트 등록
     * @author 토마스
     */
    @ApiOperation(value = "코멘트 등록", notes = "코멘트 내용을 입력해 등록 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 클립보드입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("")
    public ApplicationResponse<CommentRes> createComment(@RequestBody @Validated CreateCommentReq createCommentReq){
        return commentService.createComment(createCommentReq);
    }


    /**
     * 코멘트 전체 조회
     * @author 토마스
     */
    @ApiOperation(value = "코멘트 전체 조회", notes = "클립보드 아이디를 입력해 코멘트 전체조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 클립보드입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "clipBoardId", required = true, dataTypeClass = Long.class, example = "1")
    })
    @GetMapping("/clipboard/{clipBoardId}/user/{userId}")
    public ApplicationResponse<List<CommentRes>> findAllComments(@PathVariable("clipBoardId") Long clipBoardId, @PathVariable("userId") Long userId){
        return commentService.findAllComments(clipBoardId, userId);
    }


    /**
     * 코멘트 삭제
     * @author 토마스
     */
    @ApiOperation(value = "코멘트 삭제", notes = "코멘트 아이디를 입력해 코멘트 삭제 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 클립보드입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "clipBoardId", required = true, dataTypeClass = Long.class, example = "1")
    })
    @PatchMapping("/{commentId}")
    public ApplicationResponse<DeleteCommentRes> deleteComment(@PathVariable("commentId") Long commentId, @RequestBody @Validated DeleteCommentReq deleteCommentReq){
        return commentService.deleteComment(deleteCommentReq, commentId);
    }
}
