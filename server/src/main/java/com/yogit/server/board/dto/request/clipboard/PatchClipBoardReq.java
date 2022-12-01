package com.yogit.server.board.dto.request.clipboard;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PatchClipBoardReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "clipBoard ID", required = true)
    private Long clipBoardId;

    @ApiModelProperty(example = "질문이 있습니다.")
    @ApiParam(value = "클립보드 제목", required = true)
    @NotBlank
    private String title;

    @ApiModelProperty(example = "경복궁역 몇 번 출구인가요?")
    @ApiParam(value = "클립보드 상세 내용", required = true)
    @NotBlank
    private String content;

    @ApiModelProperty(example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    @ApiParam(value = "애플 리프레쉬 토큰", required = true)
    private String refreshToken;
}
