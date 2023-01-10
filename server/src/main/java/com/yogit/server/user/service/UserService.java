package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.*;
import com.yogit.server.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Void validateRefreshToken(Long userId, String refreshToken);

    ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq);

    Void enterUserImage(User user, MultipartFile uploadProfileImage, List<MultipartFile> uploadImages);

    Void deleteUserImage(List<Long> deleteUserImageIds);

    ApplicationResponse<UserProfileRes> getProfile(GetUserProfileReq getUserProfileReq);

    ApplicationResponse<Void> delProfile(Long userId);

    ApplicationResponse<UserImagesRes> getUserImage(GetUserImageReq getUserImageReq);

    ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq);

    ApplicationResponse<Void> createUser(CreateUserReq createUserReq);

    User createUserApple(CreateUserAppleReq createUserAppleReq);

    ApplicationResponse<UserDeviceTokenRes> addDeviceToken(AddUserDeviceTokenReq addUserDeviceTokenReq);

    ApplicationResponse<LogoutRes> logout(LogoutReq logoutReq);
}
