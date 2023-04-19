package com.yogit.server.apns.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateClipBoardAPNReq {

    @ApiModelProperty(example = "예제 추가 예정", value = "디바이스 토큰")
    @ApiParam(required = true)
    private String destinationDeviceToken;

    private String userName;
    private Long boardId;
    private String boardName;
    private String time;
    boolean isOpened;

    @Builder
    public CreateClipBoardAPNReq(String destinationDeviceToken, String userName, Long boardId, String boardName, String time) {
        this.destinationDeviceToken = destinationDeviceToken;
        this.userName = userName;
        this.boardId = boardId;
        this.boardName = boardName;
        this.time = time;
        this.isOpened = false;
    }
}
