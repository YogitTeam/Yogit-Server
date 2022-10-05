package com.yogit.server.user.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.createUserEssentialProfileReq;
import com.yogit.server.user.dto.request.editUserEssentialProfileReq;
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
    public ApplicationResponse<UserProfileRes> enterEssentialProfile(@ModelAttribute createUserEssentialProfileReq createUserEssentialProfileReq){
        return userService.enterEssentialProfile(createUserEssentialProfileReq);
    }

    @ApiOperation(value = "유저 필수 정보 수정", notes = "수정할 정보들만 입력해주세요, Language 중에 하나라도 변경사항이 있다면 유저의 모든 Language 를 입력하여 요청해주세요")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true)
    })
    @PatchMapping("/essential-profile")
    public ApplicationResponse<UserProfileRes> editEssentialProfile(@ModelAttribute editUserEssentialProfileReq editUserEssentialProfileReq){
        return userService.editEssentialProfile(editUserEssentialProfileReq);
    }

}
