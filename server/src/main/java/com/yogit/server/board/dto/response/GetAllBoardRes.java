package com.yogit.server.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yogit.server.board.entity.Board;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetAllBoardRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "생성된 Board ID")
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "도시 ID")
    private Long cityId;

    @ApiModelProperty(example = "SEOUL")
    @ApiParam(value = "도시 Name")
    private String cityName;

    @ApiModelProperty(example = "추가 예정")
    @ApiParam(value = "유저 프로필 이미지 url")
    private String profileImgUrl;

    @ApiModelProperty(example = "경복궁 탐사입니다.")
    @ApiParam(value = "게시글 제목")
    private String title;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "사용자 ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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

    @ApiModelProperty(example = "이미지 url")
    @ApiParam(value = "게시글 첫번째 이미지 url")
    private String imageUrl;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "게시글 첫번째 이미지 ID")
    private Long imageId;

    @ApiModelProperty(example = "ACTIVE")
    @ApiParam(value = "객체 상태")
    private BaseStatus status;

    public static GetAllBoardRes toDto(Board board, String imageUrl, String profileImgUrl){
        return GetAllBoardRes.builder()
                .boardId(board.getId())
                .cityId(board.getCity().getId())
                .cityName(board.getCity().getCityName())
                .profileImgUrl(profileImgUrl)
                .title(board.getTitle())
                .date(board.getDate())
                .currentMember(board.getCurrentMember())
                .totalMember(board.getTotalMember())
                .categoryId(board.getCategory().getId())
                .imageUrl(imageUrl)
                .imageId(board.getBoardImages().get(0).getId())
                .status(board.getStatus())
                .build();
    }

    @Builder
    public GetAllBoardRes(Long boardId, Long cityId, String cityName, String profileImgUrl, String title, LocalDateTime date, int currentMember, int totalMember, Long categoryId, String imageUrl, Long imageId, BaseStatus status) {
        this.boardId = boardId;
        this.cityId = cityId;
        this.cityName = cityName;
        this.profileImgUrl = profileImgUrl;
        this.title = title;
        this.date = date;
        this.currentMember = currentMember;
        this.totalMember = totalMember;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.status = status;
    }
}
