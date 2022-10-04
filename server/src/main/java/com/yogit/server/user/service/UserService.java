package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserProfileRes;

public interface UserService {
    ApplicationResponse<UserProfileRes> enterEssentialProfile(UserEssentialProfileReq userEssentialProfileReq);
}
