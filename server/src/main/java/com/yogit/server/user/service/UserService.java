package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserEssentialProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.entity.User;

public interface UserService {
    ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(Long userId);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq);

    ApplicationResponse<UserImagesRes> getUserImage(Long userId);

    ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);

    User createUserApple(CreateUserAppleReq createUserAppleReq);

    ApplicationResponse<UserImagesRes> deleteUserImage(DeleteUserImageReq deleteUserImageReq);
}
