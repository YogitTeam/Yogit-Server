package com.yogit.server.board.dto.response.comment;

import com.yogit.server.board.entity.Comment;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCommentRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "코멘트 ID")
    private Long commentId;

    @ApiModelProperty(example = "ACTIVE")
    @ApiParam(value = "객체 상태")
    private BaseStatus status;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "생성 시각")
    private String createdAt;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "마지막 업데이트 시각")
    private String updatedAt;

    /*
    연관관계 편의 메서드
     */
    public static DeleteCommentRes toDto(Comment comment){
        return DeleteCommentRes.builder()
                .commentId(comment.getId())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    @Builder
    public DeleteCommentRes(Long commentId, BaseStatus status, String createdAt, String updatedAt) {
        this.commentId = commentId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
