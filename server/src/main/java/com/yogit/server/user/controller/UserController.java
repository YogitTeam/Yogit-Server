package com.yogit.server.user.controller;

import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(tags = "User API")
public class UserController {

    @ApiOperation(value = "유저 컨트롤러 테스트", notes = "테스트입니다.")
    @ApiResponses({
            @ApiResponse(code =1000 ,message ="요청에 성공하였습니다"),
            @ApiResponse(code =4000 ,message = "서버 오류입니다.")
    })
    @GetMapping
    public String test(){
        return "ok";
    }

    @ApiOperation(value = "유저 필수 정보 입력", notes = "languageName1 부터, languageLevel1 부터 차례로 입력해주세요 (swagger에서 enum 열거형을 지원하지 않으므로)")
    @PostMapping("/essential-profile")
    public Void essentialProfile(@ModelAttribute UserEssentialProfileReq userEssentialProfileReq){

        return null;
    }

}
