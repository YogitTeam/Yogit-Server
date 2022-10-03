package com.yogit.server.user.controller;

import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import com.yogit.server.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "유저 필수 정보 입력", notes = "languageName1 부터, languageLevel1 부터 차례로 입력해주세요 (swagger에서 enum 열거형을 지원하지 않으므로)")
    @PostMapping("/essential-profile")
    public void enterEssentialProfile(@ModelAttribute UserEssentialProfileReq userEssentialProfileReq){
        userService.enterEssentialProfile(userEssentialProfileReq);
    }

}
