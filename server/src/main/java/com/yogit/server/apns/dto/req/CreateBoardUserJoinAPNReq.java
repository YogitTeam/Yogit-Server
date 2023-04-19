package com.yogit.server.apns.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBoardUserJoinAPNReq {

    @ApiModelProperty(example = "예제 추가 예정", value = "디바이스 토큰")
    @ApiParam(required = true)
    private String destinationDeviceToken;

    private String joinUserName;
    private Long boardId;
    private String boardName;
    private String time;
    boolean isOpened;

    @Builder
    public CreateBoardUserJoinAPNReq(String destinationDeviceToken, String joinUserName, Long boardId, String boardName, String time) {
        this.destinationDeviceToken = destinationDeviceToken;
        this.joinUserName = joinUserName;
        this.boardId = boardId;
        this.boardName = boardName;
        this.time = time;
        this.isOpened = false;
    }
}
