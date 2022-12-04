package com.yogit.server.board.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBoardReq {

    @ApiModelProperty(example = "43")
    @ApiParam(value = "Board ID", required = true)
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;

    @ApiModelProperty(example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    @ApiParam(value = "애플 리프레쉬 토큰", required = true)
    private String refreshToken;
}
