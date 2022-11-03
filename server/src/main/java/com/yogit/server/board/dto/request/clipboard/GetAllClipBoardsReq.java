package com.yogit.server.board.dto.request.clipboard;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAllClipBoardsReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "Board ID", required = true)
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;
}
