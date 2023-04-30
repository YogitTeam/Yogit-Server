package com.yogit.server.board.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAllBoardsByCityReq {

    @ApiModelProperty(example = "1", value = "유저 ID")
    @ApiParam(required = true)
    private Long userId;

    @ApiModelProperty(example = "1", value = "페이징 조회 페이지")
    @ApiParam(required = true)
    private int cursor;

    @ApiModelProperty(example = "1", value = "그룹 카테고리 ID")
    @ApiParam( required = true)
    private Long categoryId;

    @ApiModelProperty(example = "SEOUL", value = "City 이름")
    @ApiParam(required = true)
    private String cityName;

    @ApiModelProperty(example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA", value = "애플 리프레쉬 토큰")
    @ApiParam(required = true)
    private String refreshToken;
}
