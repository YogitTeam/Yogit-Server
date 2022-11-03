package com.yogit.server.applelogin.service;

import com.yogit.server.applelogin.model.TokenResponse;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface AppleService {

    String getAppleClientSecret(String id_token) throws NoSuchAlgorithmException;

    TokenResponse requestCodeValidations(String client_secret, String code, String refresh_token);

    Map<String, String> getLoginMetaInfo();

    String getPayload(String id_token);

}
