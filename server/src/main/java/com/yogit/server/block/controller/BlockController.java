package com.yogit.server.block.controller;

import com.yogit.server.block.dto.req.CreateBlockReq;
import com.yogit.server.block.dto.res.BlockRes;
import com.yogit.server.block.service.BlockService;
import com.yogit.server.global.dto.ApplicationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/blocks")
public class BlockController {

    private final BlockService blockService;

    /**
     * 유저 차단 생성
     * @author 토마스
     */
    @ApiOperation(value = "유저 차단 생성", notes = "유저 차단 생성 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping
    public ApplicationResponse<BlockRes> createBlock(@RequestBody @Validated CreateBlockReq dto){
        return blockService.createBlock(dto);
    }
}
