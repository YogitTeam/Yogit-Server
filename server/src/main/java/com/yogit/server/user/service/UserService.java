package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.*;
import com.yogit.server.user.entity.User;

public interface UserService {
    Void validateRefreshToken(Long userId, String refreshToken);

    ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    ApplicationResponse<UserProfileRes> getProfile(GetUserProfileReq getUserProfileReq);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq);

    ApplicationResponse<UserImagesRes> getUserImage(GetUserImageReq getUserImageReq);

    ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);

    User createUserApple(CreateUserAppleReq createUserAppleReq);

    ApplicationResponse<UserImagesRes> deleteUserImage(DeleteUserImageReq deleteUserImageReq);

    ApplicationResponse<UserDeviceTokenRes> addDeviceToken(AddUserDeviceTokenReq addUserDeviceTokenReq);

    ApplicationResponse<LogoutRes> logout(LogoutReq logoutReq);
}
