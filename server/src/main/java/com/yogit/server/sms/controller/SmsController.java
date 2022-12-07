package com.yogit.server.sms.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.sms.dto.SmsSendReq;
import com.yogit.server.sms.dto.SmsSendRes;
import com.yogit.server.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Api(tags = "SMS 인증 API")
public class SmsController {

    private final SmsService smsService;

    @ApiOperation(value = "문자발송", notes = "문자를 발송합니다.")
    @PostMapping("/sms")
    public ApplicationResponse<SmsSendRes> send(@RequestBody SmsSendReq smsSendReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return ApplicationResponse.ok(SmsSendRes.builder()
                .value(smsService.send(smsSendReq))
                .build());
    }
//
//
//    /**
//     * 게시글 참여 알림 문자 발송
//     * @author 토마스
//     */
//    @ApiOperation(value = "게시글 참여 알림 문자 발송", notes = "참여 알림 문자를 발송합니다.")
//    @PostMapping("/sms/board")
//    public ApplicationResponse<SmsSendRes> sendBoardJoinAlarm(@RequestBody @Validated SmsSendBoardJoinAlarmReq dto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
//        return ApplicationResponse.ok(SmsSendRes.builder()
//                .value(smsService.sendBoardJoinAlarm(dto))
//                .build());
//    }
}
