package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;

public interface UserService {
    ApplicationResponse<UserProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> editEssentialProfile(EditUserEssentialProfileReq editUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(Long userId);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq);

    ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);
}
