package com.yogit.server.board.dto.request.comment;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateCommentReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "ClipBoard ID", required = true)
    private Long clipBoardId;

    @ApiModelProperty(example = "경복궁역 몇 번 출구인가요?")
    @ApiParam(value = "클립보드 상세 내용", required = true)
    @NotBlank
    private String content;

    @ApiModelProperty(example = "입력 예정")
    @ApiParam(value = "애플 refresh token", required = true)
    @NotBlank
    private String refreshToken;
}
