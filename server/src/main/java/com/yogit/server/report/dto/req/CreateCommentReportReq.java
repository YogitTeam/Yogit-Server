package com.yogit.server.report.dto.req;

import com.yogit.server.report.enums.ReportType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCommentReportReq {

    @ApiModelProperty(example = "음란물이 있습니다.")
    @ApiParam(value = "신고 세부 내용", required = false)
    private String content;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고하는 유저 ID", required = true)
    private Long reportingUserId;

    @ApiModelProperty(example = "2")
    @ApiParam(value = "신고당하는 유저 ID", required = true)
    private Long reportedUserId;

    @ApiModelProperty(example = "PORNOGRAPHY")
    @ApiParam(value = "신고 이유 타입", required = true)
    private ReportType reportType;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고당하는 코멘트 ID", required = true)
    private Long reportedCommentId;
}
