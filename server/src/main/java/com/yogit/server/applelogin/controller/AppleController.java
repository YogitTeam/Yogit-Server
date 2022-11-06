package com.yogit.server.applelogin.controller;

import com.yogit.server.applelogin.model.AppsResponse;
import com.yogit.server.applelogin.model.ServicesResponse;
import com.yogit.server.applelogin.model.TokenResponse;
import com.yogit.server.applelogin.service.AppleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class AppleController {

    private Logger logger = LoggerFactory.getLogger(AppleController.class);

    @Autowired
    AppleService appleService;

    /**
     * Sign in with Apple - JS Page (index.html)
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/")
    public String appleLoginPage(ModelMap model) {

        Map<String, String> metaInfo = appleService.getLoginMetaInfo();

        model.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
        model.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
        model.addAttribute("nonce", metaInfo.get("NONCE"));

        System.out.println(model.getAttribute("client_id"));
        System.out.println(model.getAttribute("redirect_uri"));
        System.out.println(model.getAttribute("nonce"));


        return "index";
    }

    /**
     * Apple login page Controller (SSL - https)
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/apple/login")
    public String appleLogin(ModelMap model) {

        Map<String, String> metaInfo = appleService.getLoginMetaInfo();

        model.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
        model.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
        model.addAttribute("nonce", metaInfo.get("NONCE"));
        model.addAttribute("response_type", "code id_token");
        model.addAttribute("scope", "name email");
        model.addAttribute("response_mode", "form_post");

        System.out.println("==========================");
        System.out.println(model.getAttribute("client_id"));
        System.out.println(model.getAttribute("redirect_uri"));
        System.out.println(model.getAttribute("nonce"));
        System.out.println(model.getAttribute("response_type"));
        System.out.println(model.getAttribute("scope"));
        System.out.println(model.getAttribute("response_mode"));


        return "redirect:https://appleid.apple.com/auth/authorize";
    }

    /**
     * Apple Login 유저 정보를 받은 후 권한 생성
     *
     * @param serviceResponse
     * @return
     */
    @PostMapping(value = "/redirect")
    @ResponseBody
    public TokenResponse servicesRedirect(ServicesResponse serviceResponse) throws NoSuchAlgorithmException {

        System.out.println("1-------------");
        if (serviceResponse == null) {
            return null;
        }
        System.out.println("2-------------");


        System.out.println(serviceResponse);
        System.out.println("3-------------");


        String code = serviceResponse.getCode();
        System.out.println(code);
        System.out.println("4-------------");

        String id_token = serviceResponse.getId_token();
        System.out.println(id_token);
        System.out.println("5-------------");

        String client_secret = appleService.getAppleClientSecret(serviceResponse.getId_token());
        System.out.println(client_secret);
        System.out.println("6-------------");


        logger.debug("================================");
        logger.debug("id_token ‣ " + serviceResponse.getId_token());
        logger.debug("payload ‣ " + appleService.getPayload(serviceResponse.getId_token()));
        logger.debug("client_secret ‣ " + client_secret);
        logger.debug("================================");

        System.out.println("7-------------");

        return appleService.requestCodeValidations(client_secret, code, null);
    }

    /**
     * refresh_token 유효성 검사
     *
     * @param client_secret
     * @param refresh_token
     * @return
     */
    @PostMapping(value = "/refresh")
    @ResponseBody
    public TokenResponse refreshRedirect(@RequestParam String client_secret, @RequestParam String refresh_token) {
        return appleService.requestCodeValidations(client_secret, null, refresh_token);
    }

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
