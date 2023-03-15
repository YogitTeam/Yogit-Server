package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.*;
import com.yogit.server.user.entity.User;

public interface UserService {

    ApplicationResponse<UserProfileRes> enterProfile(CreateUserProfileReq createUserProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(GetUserProfileReq getUserProfileReq);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> getUserImage(GetUserImageReq getUserImageReq);

    ApplicationResponse<UserImagesRes> AddAndDeleteUserImage(AddAndDeleteUserImageReq addAndDeleteUserImageReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);

    User createUserApple(CreateUserAppleReq createUserAppleReq);

    ApplicationResponse<UserDeviceTokenRes> addDeviceToken(AddUserDeviceTokenReq addUserDeviceTokenReq);

    ApplicationResponse<LogoutRes> logout(LogoutReq logoutReq);
}
