package com.yogit.server.user.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "유저 필수 정보 입력", notes = "languageName1 부터, languageLevel1 부터 차례로 입력해주세요 (swagger에서 enum 열거형을 지원하지 않으므로)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", required = true),
            @ApiImplicitParam(name = "userAge", required = true),
            @ApiImplicitParam(name = "gender", required = true),
            @ApiImplicitParam(name = "nationality", required = true),
            @ApiImplicitParam(name = "languageName1", required = true),
            @ApiImplicitParam(name = "languageLevel1", required = true)
    })
    @PostMapping("/essential-profile")
    public ApplicationResponse<UserProfileRes> enterEssentialProfile(@ModelAttribute UserEssentialProfileReq userEssentialProfileReq){
        return userService.enterEssentialProfile(userEssentialProfileReq);
    }

//    @PatchMapping("/essential-profile")
//    public ApplicationResponse<>

}
