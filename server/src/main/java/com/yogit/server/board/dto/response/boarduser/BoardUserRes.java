package com.yogit.server.board.dto.response.boarduser;

import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    @ApiModelProperty(example = "[1,2,3]")
    @ApiParam(value = "참여 유저 이미지 ID 리스트")
    private List<Long> userIds;

    @ApiModelProperty(example = "['이미지 url','이미지2 url']")
    @ApiParam(value = "참여 유저 이미지 url 리스트")
    private List<String> userImageUrls;

    @Builder
    public BoardUserRes(Long userId, Long boardId, Long boardUserId, List<Long> userIds, List<String> userImageUrls) {
        this.userId = userId;
        this.boardId = boardId;
        this.boardUserId = boardUserId;
        this.userIds = userIds;
        this.userImageUrls = userImageUrls;
    }


    public static BoardUserRes toDto(BoardUser boardUser, User user, Board board, List<User> users, List<String> userImageUrls){
        return BoardUserRes.builder()
                .userId(user.getId())
                .boardId(board.getId())
                .boardUserId(boardUser.getId())
                .userIds(users.stream().map(User::getId).collect(Collectors.toList()))
                .userImageUrls(userImageUrls)
                .build();
    }
}
