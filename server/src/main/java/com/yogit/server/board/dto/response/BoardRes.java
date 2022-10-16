package com.yogit.server.board.dto.response;

import com.yogit.server.board.entity.Board;
import com.yogit.server.config.domain.BaseStatus;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardRes {
    @ApiModelProperty(example = "1")
    @ApiParam(value = "생성된 Board ID")
    private Long boardId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "도시 ID")
    private Long cityId;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "호스트 ID")
    private Long hostId;

    @ApiModelProperty(example = "경복궁 탐사입니다.")
    @ApiParam(value = "게시글 제목")
    private String title;

    @ApiModelProperty(example = "서울특별시 종로구 사직로 130")
    @ApiParam(value = "모임 주소")
    private String address;

    @ApiModelProperty(example = "37.1")
    @ApiParam(value = "위도")
    private float longitute;

    @ApiModelProperty(example = "37")
    @ApiParam(value = "경도")
    private float latitude;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "사용자 ID")
    private LocalDateTime date; // 모임 시각

    @ApiModelProperty(example = "시간에 맞춰오시기 바랍니다.")
    @ApiParam(value = "모임 공지사항")
    private String notice;

    @ApiModelProperty(example = "3시에 모여서 경복궁역에서 경복궁으로 출발합니다.")
    @ApiParam(value = "모임 상세설명")
    private String introduction; // 게시글 내용 상세설명

    @ApiModelProperty(example = "활발한 사람이 오면 좋습니다.")
    @ApiParam(value = "원하는 사람 설명")
    private String kindOfPerson; // 이런 사람을 원합니다 설명 글.

    @ApiModelProperty(example = "2")
    @ApiParam(value = "현재 참여 인원수")
    private int currentMember;

    @ApiModelProperty(example = "5")
    @ApiParam(value = "총 인원수")
    private int totalMember;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "모임 카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(example = "ACTIVE")
    @ApiParam(value = "객체 상태")
    private BaseStatus status;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "생성 시각")
    private String createdAt;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "마지막 업데이트 시각")
    private String updatedAt;


    public static BoardRes toDto(Board board){
        return BoardRes.builder()
                .boardId(board.getId())
                .cityId(board.getCity().getId())
                .hostId(board.getHost().getId())
                .title(board.getTitle())
                .address(board.getAddress())
                .longitute(board.getLongitute())
                .latitude(board.getLatitude())
                .date(board.getDate())
                .notice(board.getNotice())
                .introduction(board.getIntroduction())
                .kindOfPerson(board.getKindOfPerson())
                .currentMember(board.getCurrentMember())
                .totalMember(board.getTotalMember())
                .categoryId(board.getCategory().getId())
                .status(board.getStatus())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    @Builder
    public BoardRes(Long boardId, Long cityId, Long hostId, String title, String address, float longitute, float latitude, LocalDateTime date, String notice, String introduction, String kindOfPerson, int currentMember, int totalMember, Long categoryId, BaseStatus status, String createdAt, String updatedAt) {
        this.boardId = boardId;
        this.cityId = cityId;
        this.hostId = hostId;
        this.title = title;
        this.address = address;
        this.longitute = longitute;
        this.latitude = latitude;
        this.date = date;
        this.notice = notice;
        this.introduction = introduction;
        this.kindOfPerson = kindOfPerson;
        this.currentMember = currentMember;
        this.totalMember = totalMember;
        this.categoryId = categoryId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
