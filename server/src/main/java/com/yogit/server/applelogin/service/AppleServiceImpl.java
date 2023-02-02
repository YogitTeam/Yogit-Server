package com.yogit.server.applelogin.service;

import com.yogit.server.applelogin.model.Account;
import com.yogit.server.applelogin.model.DeleteUserReq;
import com.yogit.server.applelogin.model.ServicesResponse;
import com.yogit.server.applelogin.model.TokenResponse;
import com.yogit.server.applelogin.util.AppleUtils;
import com.yogit.server.user.dto.request.CreateUserAppleReq;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.entity.UserStatus;
import com.yogit.server.user.entity.UserType;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppleServiceImpl implements AppleService {

    private final AppleUtils appleUtils;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${APPLE.AUD}")
    String client_id;

    /**
     * 유효한 id_token인 경우 client_secret 생성
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
     */
    @Override
    @Transactional
    public TokenResponse requestCodeValidations(ServicesResponse serviceResponse, String refresh_token) throws NoSuchAlgorithmException {

        TokenResponse tokenResponse = new TokenResponse();

        String code = serviceResponse.getCode();
        String client_secret = getAppleClientSecret(serviceResponse.getId_token());

        JSONObject user = new JSONObject(serviceResponse.getUser());
        User saveduser = null;

        // 이메일 추출
        String email = user.getAsString("email");

        // 이름 추출
        Map<String, String> name = (Map<String, String>) user.get("name");
        String lastName = name.get("lastName");
        String firstName = name.get("firstName");
        String fullName = lastName + firstName;

        // 만약 처음 인증하는 유저여서  refresh 토큰 없으면 client_secret, authorization_code로 검증
        if (client_secret != null && code != null && refresh_token == null) {
            tokenResponse = appleUtils.validateAuthorizationGrantCode(client_secret, code);

            // 유저 생성
            CreateUserAppleReq createUserAppleReq = new CreateUserAppleReq(email, tokenResponse.getRefresh_token(),fullName, UserType.APPLE);
            saveduser = userService.createUserApple(createUserAppleReq);
        }
        // 이미 refresh 토큰 있는 유저면 client_secret, refresh_token로 검증
        else if (client_secret != null && code == null && refresh_token != null) {
            tokenResponse = appleUtils.validateAnExistingRefreshToken(client_secret, refresh_token);
        }

        tokenResponse.setUserType(UserType.APPLE.toString());
        tokenResponse.setUserStatus(UserStatus.LOGIN);

        if(refresh_token == null){
            tokenResponse.setAccount(new Account(serviceResponse.getState(), code, tokenResponse.getId_token(), user, serviceResponse.getIdentifier(), false));
            tokenResponse.setUserId(saveduser.getId());
            saveduser.changeUserStatus(UserStatus.LOGIN);
        }
        else{
            tokenResponse.setAccount(new Account(serviceResponse.getState(), code, tokenResponse.getId_token(), user, serviceResponse.getIdentifier(), true));
            User findUser = userRepository.findByAppleRefreshToken(refresh_token)
                    .orElseThrow(() -> new NotFoundUserException());
            tokenResponse.setUserId(findUser.getId());
            tokenResponse.setUserName(findUser.getName());
            findUser.changeUserStatus(UserStatus.LOGIN);
        }

        return tokenResponse;
    }

    /**
     * Apple login page 호출을 위한 Meta 정보 가져오기
     */
    @Override
    public Map<String, String> getLoginMetaInfo() {
        return appleUtils.getMetaInfo();
    }

    /**
     * id_token에서 payload 데이터 가져오기
     */
    @Override
    public String getPayload(String id_token) {
        return appleUtils.decodeFromIdToken(id_token).toString();
    }

//    /**
//     * 리프레시 토큰 검증
//     *
//     * refresh_token은 만료되지 않기 때문에 권한이 필요한 요청일 경우
//     * 굳이 매번 애플 ID 서버로부터 refresh_token을 통해 access_token을 발급 받기보다는
//     * 유저의 refresh_token을 따로 DB나 기타 저장소에 저장해두고 캐싱해두고 조회해서 검증하는편이 성능면에서 낫다는 자료를 참고
//     * https://hwannny.tistory.com/71
//     */
//    @Override
//    public Void validateRefreshToken(Long userId, String refreshToken){
//        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundUserException());
//
//        if(user.getRefreshToken() == null) throw new NotFoundRefreshTokenException();
//
//        if(!user.getRefreshToken().equals(refreshToken)) throw new InvalidRefreshTokenException();
//
//        return null;
//    }

    public void deleteUser(DeleteUserReq deleteUserReq) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String revokeUrl = "https://appleid.apple.com/auth/revoke";

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", client_id);
        params.add("client_secret", appleUtils.createClientSecret());
        params.add("token", deleteUserReq.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        restTemplate.postForEntity(revokeUrl, httpEntity, String.class);

        // 유저 정보 삭제 및 유저 상태 변경 (DELETE)
        userService.validateRefreshToken(deleteUserReq.getUserId(), deleteUserReq.getRefreshToken());
        User user = userRepository.findByUserId(deleteUserReq.getUserId()).orElseThrow(() -> new NotFoundUserException());
        user.deleteUser();
    }
}
