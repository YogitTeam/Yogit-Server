package com.yogit.server.board.dto.request.boardimage;

import com.yogit.server.board.entity.BoardImage;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteBoardImageRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "게시글 이미지 id")
    private Long boardImageId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "게시글 id")
    private Long boardId;

    @ApiModelProperty(example = "예제 넣을 예정")
    @ApiParam(value = "게시글 이미지 url")
    private String imgUrl;

    @Builder
    public DeleteBoardImageRes(Long boardImageId, Long boardId, String imgUrl) {
        this.boardImageId = boardImageId;
        this.boardId = boardId;
        this.imgUrl = imgUrl;
    }

    public static DeleteBoardImageRes toDto(BoardImage boardImage, String imgUrl) {
        return DeleteBoardImageRes.builder()
                .boardImageId(boardImage.getId())
                .boardId(boardImage.getBoard().getId())
                .imgUrl(imgUrl)
                .build();
    }
}
