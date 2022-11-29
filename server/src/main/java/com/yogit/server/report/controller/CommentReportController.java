package com.yogit.server.report.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.report.dto.req.CreateCommentReportReq;
import com.yogit.server.report.dto.res.CommentReportRes;
import com.yogit.server.report.service.comment.CommentReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/commentreports")
public class CommentReportController {

    private final CommentReportService commentReportService;

    /**
     * 코멘트 신고 생성
     * @author 토마스
     */
    @ApiOperation(value = "코멘트 신고 생성", notes = "코멘트 신고 생성 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 코멘트입니다."),
            @ApiResponse(code= 404, message = "이미 신고한 유저입니다."),
            @ApiResponse(code= 404, message = "이미 신고한 코멘트입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping
    public ApplicationResponse<CommentReportRes> createCommentReport(@RequestBody @Validated CreateCommentReportReq dto){
        return commentReportService.createCommentReport(dto);
    }

}
