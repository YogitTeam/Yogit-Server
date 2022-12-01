package com.yogit.server.applelogin.service;

import com.yogit.server.applelogin.model.ServicesResponse;
import com.yogit.server.applelogin.model.TokenResponse;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface AppleService {

    String getAppleClientSecret(String id_token) throws NoSuchAlgorithmException;

    TokenResponse requestCodeValidations(ServicesResponse serviceResponse, String refresh_token) throws NoSuchAlgorithmException;

    Map<String, String> getLoginMetaInfo();

    String getPayload(String id_token);

//    Void validateRefreshToken(Long userId, String refreshToken);
}
