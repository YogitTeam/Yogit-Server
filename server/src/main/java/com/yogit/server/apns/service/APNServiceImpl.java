package com.yogit.server.apns.service;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.dto.req.CreateClipBoardAPNReq;
import com.yogit.server.apns.dto.req.DelBoardUserJoinAPNReq;
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

        String[] locArgs = new String[2];
        locArgs[0] = dto.getJoinUserName();
        locArgs[1] = dto.getBoardName();
        payloadBuilder.setLocalizedAlertTitle("GATHERING_JOIN_TITLE", new String[1]);
        payloadBuilder.setLocalizedAlertMessage("GATHERING_JOIN_BODY", locArgs);

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
    @Transactional
    public ApplicationResponse<String> delBoardUserJoinAPN(DelBoardUserJoinAPNReq dto) throws ExecutionException, InterruptedException {

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

        String[] locArgs = new String[2];
        locArgs[0] = dto.getDelUserName();
        locArgs[1] = dto.getBoardName();
        payloadBuilder.setLocalizedAlertTitle("GATHERING_WITHDRAW_TITLE", new String[1]);
        payloadBuilder.setLocalizedAlertMessage("GATHERING_WITHDRAW_BODY", locArgs);

        payloadBuilder.addCustomProperty("boardId", dto.getBoardId());
        payloadBuilder.addCustomProperty("pushType", PushType.DELAPPLY.toString());
        val payload = payloadBuilder.buildWithDefaultMaximumLength();

        val token = TokenUtil.sanitizeTokenString(dto.getDestinationDeviceToken());
        val pushNotification = new SimpleApnsPushNotification(token, APP_BUNDLE_ID, payload);
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        // 메시지를 동기로 전송
        val response = sendNotificationFuture.get();

        // 메시지를 비동기로 전송
        //        sendNotificationFuture.addListener { future ->
        //                val response = future.now
        //            println(response);
        //        }
        sendNotificationFuture.addListener(future ->
                System.out.println("getNow는 == "+future.getNow()));

        return ApplicationResponse.ok("애플 푸쉬 알람 성공");
    }

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<String> createClipBoardAPN(CreateClipBoardAPNReq dto) throws ExecutionException, InterruptedException {

        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

//        String[] locArgs = new String[2];
//        locArgs[0] = dto.getUserName();
//        locArgs[1] = dto.getBoardName();
        payloadBuilder.setLocalizedAlertTitle("CLIPBOARD_CHATTING_TITLE", "");
        //payloadBuilder.setLocalizedAlertMessage("CLIPBOARD_CHATTING_BODY", dto.getUserName(), dto.getBoardName());
        payloadBuilder.setLocalizedAlertMessage("CLIPBOARD_CHATTING_BODY", dto.getUserName());

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
