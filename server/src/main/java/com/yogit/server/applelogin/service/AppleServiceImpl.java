package com.yogit.server.applelogin.service;

import com.yogit.server.applelogin.model.TokenResponse;
import com.yogit.server.applelogin.util.AppleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class AppleServiceImpl implements AppleService {

    @Autowired
    AppleUtils appleUtils;

    /**
     * 유효한 id_token인 경우 client_secret 생성
     *
     * @param id_token
     * @return
     */
    @Override
    public String getAppleClientSecret(String id_token) throws NoSuchAlgorithmException {

        if (appleUtils.verifyIdentityToken(id_token)) {
            return appleUtils.createClientSecret();
        }

        return null;
    }

    /**
     * code 또는 refresh_token가 유효한지 Apple Server에 검증 요청
     *
     * @param client_secret
     * @param code
     * @param refresh_token
     * @return
     */
    @Override
    public TokenResponse requestCodeValidations(String client_secret, String code, String refresh_token) {

        TokenResponse tokenResponse = new TokenResponse();

        // 만약 처음 인증하는 유저여서  refresh토큰 없으면 client_secret, authorization_code로 검증
        if (client_secret != null && code != null && refresh_token == null) {
            tokenResponse = appleUtils.validateAuthorizationGrantCode(client_secret, code);
        }
        // 이미 refresh토큰잇는 유저면 client_secret, refresh_token로 검증
        else if (client_secret != null && code == null && refresh_token != null) {
            tokenResponse = appleUtils.validateAnExistingRefreshToken(client_secret, refresh_token);
        }

        return tokenResponse;
    }

    /**
     * Apple login page 호출을 위한 Meta 정보 가져오기
     *
     * @return
     */
    @Override
    public Map<String, String> getLoginMetaInfo() {
        return appleUtils.getMetaInfo();
    }

    /**
     * id_token에서 payload 데이터 가져오기
     *
     * @return
     */
    @Override
    public String getPayload(String id_token) {
        return appleUtils.decodeFromIdToken(id_token).toString();
    }
}
