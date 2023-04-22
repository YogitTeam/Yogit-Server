package com.yogit.server.apns.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DelBoardUserJoinAPNReq {

    @ApiModelProperty(example = "예제 추가 예정", value = "디바이스 토큰")
    @ApiParam(required = true)
    private String destinationDeviceToken;

    private String delUserName;
    private Long boardId;
    private String boardName;

    @Builder
    public DelBoardUserJoinAPNReq(String destinationDeviceToken, String delUserName, Long boardId, String boardName) {
        this.destinationDeviceToken = destinationDeviceToken;
        this.delUserName = delUserName;
        this.boardId = boardId;
        this.boardName = boardName;
    }
}
