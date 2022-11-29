package com.yogit.server.report.dto.res;

import com.yogit.server.report.entity.CommentReport;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentReportRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고 ID")
    private Long commentReportId;

    /*
    연관관계 편의 메서드
     */
    @Builder
    public CommentReportRes(Long commentReportId) {
        this.commentReportId = commentReportId;
    }

    public static CommentReportRes toDto(CommentReport commentReport){
        return CommentReportRes.builder()
                .commentReportId(commentReport.getId())
                .build();
    }
}
