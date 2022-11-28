package com.yogit.server.report.dto.res;

import com.yogit.server.report.entity.BoardReport;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardReportRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고 ID")
    private Long boardReportId;

    /*
    연관관계 편의 메서드
     */
    @Builder
    public BoardReportRes(Long boardReportId) {
        this.boardReportId = boardReportId;
    }

    public static BoardReportRes toDto(BoardReport boardReport){
        return BoardReportRes.builder()
                .boardReportId(boardReport.getId())
                .build();
    }
}
