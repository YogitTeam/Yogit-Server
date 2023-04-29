package com.yogit.server.board.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAllBoardsByCityReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "페이징 조회 페이지", required = true)
    private int cursor;

    @ApiModelProperty(example = "SEOUL")
    @ApiParam(value = "City 이름", required = true)
    private String cityName;

    @ApiModelProperty(example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    @ApiParam(value = "애플 리프레쉬 토큰", required = true)
    private String refreshToken;
}
