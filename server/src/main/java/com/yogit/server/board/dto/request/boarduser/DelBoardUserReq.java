package com.yogit.server.board.dto.request.boarduser;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DelBoardUserReq {
    @ApiModelProperty(example = "1")
    @ApiParam(value = "userId,유저 PK", required = true)
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "boardId, 보드 PK", required = true)
    private Long boardId;
}
