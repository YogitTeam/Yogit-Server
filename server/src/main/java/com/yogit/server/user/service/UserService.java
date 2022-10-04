package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.UserEssentialProfileReq;

public interface UserService {
    ApplicationResponse<Void> enterEssentialProfile(UserEssentialProfileReq userEssentialProfileReq);
}
