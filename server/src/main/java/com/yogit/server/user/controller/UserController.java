package com.yogit.server.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Api(value = "UserController")
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
}
