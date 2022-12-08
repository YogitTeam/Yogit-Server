package com.yogit.server.board.dto.response.clipboard;


import com.yogit.server.board.dto.response.comment.CommentRes;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClipBoardRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "ClipBoard ID")
    private Long clipBoardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "유저 ID")
    private Long userId;

    @ApiModelProperty(example = "Park jun")
    @ApiParam(value = "유저 이름")
    private String userName;

    @ApiModelProperty(example = "추가 예정")
    @ApiParam(value = "유저 프로필 이미지 url")
    private String profileImgUrl;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "Board ID")
    private Long boardId;

    @ApiModelProperty(example = "질문이 있습니다.")
    @ApiParam(value = "클립보드 제목")
    private String title;

    @ApiModelProperty(example = "경복궁역 몇 번 출구인가요?")
    @ApiParam(value = "클립보드 상세 내용")
    private String content;

    @ApiModelProperty(example = "댓글1")
    @ApiParam(value = "코멘트들")
    private List<CommentRes> commentResList;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "클립보드에 달린 코멘트 갯수")
    private Integer commentCnt;

    @ApiModelProperty(example = "ACTIVE")
    @ApiParam(value = "객체 상태")
    private BaseStatus status;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "생성 시각")
    private String createdAt;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "마지막 업데이트 시각")
    private String updatedAt;

    public static ClipBoardRes toDto(ClipBoard clipBoard, List<CommentRes> commentResList, String profileImgUrl){
        return ClipBoardRes.builder()
                .clipBoardId(clipBoard.getId())
                .userId(clipBoard.getUser().getId())
                .userName(clipBoard.getUser().getName())
                .profileImgUrl(profileImgUrl)
                .boardId(clipBoard.getBoard().getId())
                .title(clipBoard.getTitle())
                .content(clipBoard.getContent())
                .commentResList(commentResList)
                .commentCnt(commentResList.size())
                .status(clipBoard.getStatus())
                .createdAt(clipBoard.getCreatedAt())
                .updatedAt(clipBoard.getUpdatedAt())
                .build();
    }

    @Builder
    public ClipBoardRes(Long clipBoardId, Long userId, String userName, String profileImgUrl, Long boardId, String title, String content, List<CommentRes> commentResList, Integer commentCnt, BaseStatus status, String createdAt, String updatedAt) {
        this.clipBoardId = clipBoardId;
        this.userId = userId;
        this.userName = userName;
        this.profileImgUrl = profileImgUrl;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.commentResList = commentResList;
        this.commentCnt = commentCnt;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
