package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.AddUserAdditionalProfileReq;
import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.dto.request.CreateUserImageReq;
import com.yogit.server.user.dto.request.CreateUserReq;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserEssentialProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;

public interface UserService {
    ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(Long userId);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq);

    ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);
}
