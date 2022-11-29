package com.yogit.server.report.dto.res;

import com.yogit.server.report.entity.ClipBoardReport;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClipBoardReportRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고 ID")
    private Long clipBoardReportId;

    /*
    연관관계 편의 메서드
     */
    @Builder
    public ClipBoardReportRes(Long clipBoardReportId) {
        this.clipBoardReportId = clipBoardReportId;
    }

    public static ClipBoardReportRes toDto(ClipBoardReport clipBoardReport){
        return ClipBoardReportRes.builder()
                .clipBoardReportId(clipBoardReport.getId())
                .build();
    }
}
