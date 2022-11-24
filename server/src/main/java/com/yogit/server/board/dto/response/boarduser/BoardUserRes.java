package com.yogit.server.board.dto.response.boarduser;

import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardUserRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "userId,유저 PK")
    private Long userId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "boardId, 보드 PK")
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "boardUserId, 보드 멤버 PK")
    private Long boardUserId;

    public static BoardUserRes toDto(BoardUser boardUser, User user, Board board){
        return BoardUserRes.builder()
                .userId(user.getId())
                .boardId(board.getId())
                .boardUserId(boardUser.getId())
                .build();
    }

    @Builder
    public BoardUserRes(Long userId, Long boardId, Long boardUserId) {
        this.userId = userId;
        this.boardId = boardId;
        this.boardUserId = boardUserId;
    }
}
