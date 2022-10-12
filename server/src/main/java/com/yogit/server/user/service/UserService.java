package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.dto.request.CreateUserImageReq;
import com.yogit.server.user.dto.request.EditUserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;

public interface UserService {
    ApplicationResponse<UserProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> editEssentialProfile(EditUserEssentialProfileReq editUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(Long userId);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq);
}
