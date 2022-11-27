package com.yogit.server.board.dto.response;

import com.yogit.server.board.entity.Board;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GetAllBoardRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "생성된 Board ID")
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "도시 ID")
    private Long cityId;

    @ApiModelProperty(example = "추가 예정")
    @ApiParam(value = "유저 프로필 이미지 url")
    private String profileImgUrl;

    @ApiModelProperty(example = "경복궁 탐사입니다.")
    @ApiParam(value = "게시글 제목")
    private String title;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "사용자 ID")
    private LocalDateTime date; // 모임 시각

    @ApiModelProperty(example = "2")
    @ApiParam(value = "현재 참여 인원수")
    private int currentMember;

    @ApiModelProperty(example = "5")
    @ApiParam(value = "총 인원수")
    private int totalMember;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "모임 카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(example = "['이미지 url','이미지2 url']")
    @ApiParam(value = "게시글 이미지 url 리스트")
    private List<String> imageUrls;

    @ApiModelProperty(example = "[1,2,3]")
    @ApiParam(value = "게시글 이미지 ID 리스트")
    private List<Long> imageIds;

    @ApiModelProperty(example = "ACTIVE")
    @ApiParam(value = "객체 상태")
    private BaseStatus status;

    public static GetAllBoardRes toDto(Board board, List<String> imageUrls, String profileImgUrl){
        return GetAllBoardRes.builder()
                .boardId(board.getId())
                .cityId(board.getCity().getId())
                .profileImgUrl(profileImgUrl)
                .title(board.getTitle())
                .date(board.getDate())
                .currentMember(board.getCurrentMember())
                .totalMember(board.getTotalMember())
                .categoryId(board.getCategory().getId())
                .imageUrls(imageUrls)
                .imageIds(board.getBoardImages().stream().map(image -> image.getId()).collect(Collectors.toList()))
                .status(board.getStatus())
                .build();
    }

    @Builder
    public GetAllBoardRes(Long boardId, Long cityId, String profileImgUrl, String title, LocalDateTime date, int currentMember, int totalMember, Long categoryId, List<String> imageUrls, List<Long> imageIds, BaseStatus status) {
        this.boardId = boardId;
        this.cityId = cityId;
        this.profileImgUrl = profileImgUrl;
        this.title = title;
        this.date = date;
        this.currentMember = currentMember;
        this.totalMember = totalMember;
        this.categoryId = categoryId;
        this.imageUrls = imageUrls;
        this.imageIds = imageIds;
        this.status = status;
    }
}
