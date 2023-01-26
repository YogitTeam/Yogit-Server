package com.yogit.server.board.dto.response;

import com.yogit.server.board.entity.Board;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteBoardRes {

    @ApiModelProperty(example = "ACTIVE", value = "객체 상태")
    private BaseStatus status;

    @Builder
    public DeleteBoardRes(BaseStatus status) {
        this.status = status;
    }

    public static DeleteBoardRes toDto(Board board){
        return DeleteBoardRes.builder()
                .status(board.getStatus())
                .build();
    }
}
