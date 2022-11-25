package com.yogit.server.report.dto.res;

import com.yogit.server.report.entity.UserReport;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserReportRes {

    @ApiModelProperty(example = "1")
    @ApiParam(value = "신고 ID")
    private Long userReportId;

    /*
    연관관계 편의 메서드
     */
    @Builder
    public UserReportRes(Long userReportId) {
        this.userReportId = userReportId;
    }

    public static UserReportRes toDto(UserReport userReport){
        return UserReportRes.builder()
                .userReportId(userReport.getId())
                .build();
    }
}
