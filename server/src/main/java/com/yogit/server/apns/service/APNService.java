package com.yogit.server.apns.service;

import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.dto.req.CreateClipBoardAPNReq;
import com.yogit.server.apns.dto.req.DelBoardUserJoinAPNReq;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.concurrent.ExecutionException;

public interface APNService {

    ApplicationResponse<String> createApplePushNotification() throws ExecutionException, InterruptedException;

    ApplicationResponse<String> createBoardUserJoinAPN(CreateBoardUserJoinAPNReq createBoardUserJoinAPNReq) throws ExecutionException, InterruptedException;
    ApplicationResponse<String> delBoardUserJoinAPN(DelBoardUserJoinAPNReq delBoardUserJoinAPNReq) throws ExecutionException, InterruptedException;

    ApplicationResponse<String> createClipBoardAPN(CreateClipBoardAPNReq createClipBoardAPNReq) throws ExecutionException, InterruptedException;
}
