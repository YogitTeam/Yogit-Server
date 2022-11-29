package com.yogit.server.block.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBlockReq {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "차단 생성하는 유저 ID", required = true)
    private Long blockingUserId;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "차단 받는 유저 ID", required = true)
    private Long blockedUserId;
}
