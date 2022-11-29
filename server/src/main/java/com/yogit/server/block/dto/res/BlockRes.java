package com.yogit.server.block.dto.res;

import com.yogit.server.block.entity.Block;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlockRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "차단 객체 ID")
    private Long blockId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "차단 생성하는 유저 ID")
    private Long blockingUserId;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "차단 받는 유저 ID")
    private Long blockedUserId;

    /*
     연관관계 편의 메서드
     */
    @Builder
    public BlockRes(Long blockId, Long blockingUserId, Long blockedUserId) {
        this.blockId = blockId;
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
    }

    public static BlockRes toDto(Block block){
        return BlockRes.builder()
                .blockId(block.getId())
                .blockingUserId(block.getBlockingUser().getId())
                .blockedUserId(block.getBlockedUser().getId())
                .build();
    }
}
