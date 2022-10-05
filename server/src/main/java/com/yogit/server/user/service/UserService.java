package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.createUserEssentialProfileReq;
import com.yogit.server.user.dto.request.editUserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserProfileRes;

public interface UserService {
    ApplicationResponse<UserProfileRes> enterEssentialProfile(createUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> editEssentialProfile(editUserEssentialProfileReq editUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(Long userId);

    ApplicationResponse<Void> delProfile(Long userId);
}
