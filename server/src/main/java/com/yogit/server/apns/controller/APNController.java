package com.yogit.server.apns.controller;

import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.service.APNService;
import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.global.dto.ApplicationResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/apns")
public class APNController {

    private final APNService apnService;

    /**
     * apn 푸쉬 알람 생성
     * @author 토마스
     */
    @ApiOperation(value = "apn 푸쉬 알람 생성", notes = "apn 푸쉬 알람 생성 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping
    public ApplicationResponse<String> createAPN() throws ExecutionException, InterruptedException {
        return apnService.createApplePushNotification();
    }

    /**
     * apn 참여자 보드 참여 신청 호스트에게 푸쉬 알림
     * @author 토마스
     */
    @ApiOperation(value = "apn 참여자 보드 참여 신청 호스트에게 푸쉬 알림", notes = "apn 푸쉬 알람 생성 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code= 404, message = "요청한 유저가 호스트가 아닙니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/boarduser-join")
    public ApplicationResponse<String> createBoardUserJoinAPN(@RequestBody @Validated CreateBoardUserJoinAPNReq dto) throws ExecutionException, InterruptedException {
        return apnService.createBoardUserJoinAPN(dto);
    }
}
