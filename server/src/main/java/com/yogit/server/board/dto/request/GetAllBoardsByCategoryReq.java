package com.yogit.server.board.dto.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAllBoardsByCategoryReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID", required = true)
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "페이징 조회 페이지", required = true)
    private int cursor;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "그룹 카테고리 ID", required = true)
    private Long categoryId;
}
