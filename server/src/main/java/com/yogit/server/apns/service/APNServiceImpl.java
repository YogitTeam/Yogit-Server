package com.yogit.server.apns.service;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.dto.req.CreateClipBoardAPNReq;
import com.yogit.server.apns.entity.PushType;
import com.yogit.server.global.dto.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class APNServiceImpl implements APNService{

    private final ApnsClient apnsClient;

    @Value("${APPLE.AUD}")
    private String APP_BUNDLE_ID;

    @Value("${APN.DESTINATION.DEVICE.TOKEN}")
    private String destinationDeviceToken;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<String> createApplePushNotification() throws ExecutionException, InterruptedException {

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertTitle("test_title 푸쉬 제목");
        payloadBuilder.setAlertBody("test_body, 푸쉬 바디");
        payloadBuilder.addCustomProperty("test_data_1", "abc");
        payloadBuilder.addCustomProperty("test_data_2", "def");
        val payload = payloadBuilder.buildWithDefaultMaximumLength();

        val token = TokenUtil.sanitizeTokenString(destinationDeviceToken);
        val pushNotification = new SimpleApnsPushNotification(token, APP_BUNDLE_ID, payload);
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        // 메시지를 동기로 전송
        val response = sendNotificationFuture.get();
        System.out.println("response는  ==="+ response);

        // 메시지를 비동기로 전송
        //        sendNotificationFuture.addListener { future ->
        //                val response = future.now
        //            println(response);
        //        }
        sendNotificationFuture.addListener(future ->
                System.out.println("getNow는 == "+future.getNow()));


        // 성공 응답시 null 반환
        // 오류 응답시 BadDeviceToken 등의 오류 코드 문자열 반환
        //        println(response.rejectionReason);
        System.out.println("getRejectionReason는 === :"+response.getRejectionReason());

        // 응답에서 원본 메시지 획득 가능
        // {"aps":{"alert":{"body":"test_body","title":"test_title"}},"test_data_3":"def","test_data_1":"abc"}
        //        println(response.pushNotification.payload);
        System.out.println("getPushNotification는 === :"+response.getPushNotification());

        return ApplicationResponse.ok("애플 푸쉬 알람 성공");
    }


    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<String> createBoardUserJoinAPN(CreateBoardUserJoinAPNReq dto) throws ExecutionException, InterruptedException {

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertTitle("모임 참여 알림");
        payloadBuilder.setAlertBody(dto.getJoinUserName()+"님이 "+dto.getBoardName()+"을 신청하였습니다.");
        payloadBuilder.addCustomProperty("boardId", dto.getBoardId());
        payloadBuilder.addCustomProperty("pushType", PushType.JOINAPPLY.toString());
        val payload = payloadBuilder.buildWithDefaultMaximumLength();

        val token = TokenUtil.sanitizeTokenString(dto.getDestinationDeviceToken());
        val pushNotification = new SimpleApnsPushNotification(token, APP_BUNDLE_ID, payload);
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        // 메시지를 동기로 전송
        val response = sendNotificationFuture.get();
        System.out.println("response는  ==="+ response);

        // 메시지를 비동기로 전송
        //        sendNotificationFuture.addListener { future ->
        //                val response = future.now
        //            println(response);
        //        }
        sendNotificationFuture.addListener(future ->
                System.out.println("getNow는 == "+future.getNow()));


        // 성공 응답시 null 반환
        // 오류 응답시 BadDeviceToken 등의 오류 코드 문자열 반환
        //        println(response.rejectionReason);
        System.out.println("getRejectionReason는 === :"+response.getRejectionReason());

        // 응답에서 원본 메시지 획득 가능
        // {"aps":{"alert":{"body":"test_body","title":"test_title"}},"test_data_3":"def","test_data_1":"abc"}
        //        println(response.pushNotification.payload);
        System.out.println("getPushNotification는 === :"+response.getPushNotification());

        return ApplicationResponse.ok("애플 푸쉬 알람 성공");
    }


    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<String> createClipBoardAPN(CreateClipBoardAPNReq dto) throws ExecutionException, InterruptedException {

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
        payloadBuilder.setAlertTitle("ClipBoard Alarm");
        payloadBuilder.setAlertBody(dto.getUserName()+"님이 "+dto.getBoardName()+"에 댓글을 달았습니다.");
        payloadBuilder.addCustomProperty("boardId", dto.getBoardId());
        payloadBuilder.addCustomProperty("pushType", PushType.CREATE_CLIPBOARD.toString());
        val payload = payloadBuilder.buildWithDefaultMaximumLength();

        val token = TokenUtil.sanitizeTokenString(dto.getDestinationDeviceToken());
        val pushNotification = new SimpleApnsPushNotification(token, APP_BUNDLE_ID, payload);
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        // 메시지를 동기로 전송
        val response = sendNotificationFuture.get();
        System.out.println("response는  ==="+ response);

        // 메시지를 비동기로 전송
        //        sendNotificationFuture.addListener { future ->
        //                val response = future.now
        //            println(response);
        //        }
        sendNotificationFuture.addListener(future ->
                System.out.println("getNow는 == "+future.getNow()));

        // 성공 응답시 null 반환
        // 오류 응답시 BadDeviceToken 등의 오류 코드 문자열 반환
        //        println(response.rejectionReason);
        System.out.println("getRejectionReason는 === :"+response.getRejectionReason());

        // 응답에서 원본 메시지 획득 가능
        // {"aps":{"alert":{"body":"test_body","title":"test_title"}},"test_data_3":"def","test_data_1":"abc"}
        //        println(response.pushNotification.payload);
        System.out.println("getPushNotification는 === :"+response.getPushNotification());

        return ApplicationResponse.ok("애플 푸쉬 알람 성공");
    }

}
