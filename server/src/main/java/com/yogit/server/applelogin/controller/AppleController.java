package com.yogit.server.applelogin.controller;

import com.yogit.server.applelogin.model.AppsResponse;
import com.yogit.server.applelogin.model.ServicesResponse;
import com.yogit.server.applelogin.model.TokenResponse;
import com.yogit.server.applelogin.service.AppleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;

@Controller
public class AppleController {

    private Logger logger = LoggerFactory.getLogger(AppleController.class);

    @Autowired
    AppleService appleService;

    /**
     * Apple 회원가입
     * privateKey 로 사용자 개인 정보와 refreshToken 발급받기
     * @return
     */
    @PostMapping(value = "/sign-up/apple")
    @ResponseBody
    public TokenResponse signUpApple(@RequestBody ServicesResponse servicesResponse) throws NoSuchAlgorithmException {

        if (servicesResponse == null) { // TODO 예외처리
            System.out.println("요청 값이 없습니다.");
            return null;
        }

        return appleService.requestCodeValidations(servicesResponse, null);
    }

    /**
     * refresh_token 유효성 검사
     *
     * @param client_secret
     * @param refresh_token
     * @return
     * refresh_token은 만료되지 않기 때문에
     * 권한이 필요한 요청일 경우 굳이 매번 애플 ID 서버로부터 refresh_token을 통해 access_token을 발급 받기보다는
     * 유저의 refresh_token을 따로 DB나 기타 저장소에 저장해두고 캐싱해두고 조회해서 검증하는편이 성능면에서 낫다고 함..
     * 우리도 db에 refresh token을 따로 저장해서 사용하므로 이 메소드는 필요 없지 않을까..
     */
//    @PostMapping(value = "/refresh")
//    @ResponseBody
//    public TokenResponse refreshRedirect(@RequestParam String client_secret, @RequestParam String refresh_token) {
//        return appleService.requestCodeValidations(client_secret, null, refresh_token);
//    }

    /**
     * Apple 유저의 이메일 변경, 서비스 해지, 계정 탈퇴에 대한 Notifications을 받는 Controller (SSL - https (default: 443))
     *
     * @param appsResponse
     */
    @PostMapping(value = "/apps/to/endpoint")
    @ResponseBody
    public void appsToEndpoint(@RequestBody AppsResponse appsResponse) {
        logger.debug("[/path/to/endpoint] RequestBody ‣ " + appsResponse.getPayload());
    }

}
