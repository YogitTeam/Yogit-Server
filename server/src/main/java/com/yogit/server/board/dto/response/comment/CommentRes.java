package com.yogit.server.board.dto.response.comment;

import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import com.yogit.server.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "Comment ID")
    private Long commentId;

    @ApiModelProperty(example = "경복궁역 몇 번 출구인가요?")
    @ApiParam(value = "클립보드 상세 내용")
    private String content;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID")
    private Long userId;

    @ApiModelProperty(example = "Park jun")
    @ApiParam(value = "유저 이름")
    private String userName;

    @ApiModelProperty(example = "추가 예정")
    @ApiParam(value = "유저 프로필 이미지")
    private String profileImg;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "ClipBoard ID")
    private Long clipBoardId;

    public static CommentRes toDto(Comment comment){
        return CommentRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .profileImg(comment.getUser().getProfileImg())
                .clipBoardId(comment.getClipBoard().getId())
                .build();
    }

    @Builder
    public CommentRes(Long commentId, String content, Long userId, String userName, String profileImg, Long clipBoardId) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.profileImg = profileImg;
        this.clipBoardId = clipBoardId;
    }
}
