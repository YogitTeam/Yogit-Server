package com.yogit.server.board.controller;

import com.yogit.server.board.dto.request.boarduser.CreateBoardUserReq;
import com.yogit.server.board.dto.response.boarduser.BoardUserRes;
import com.yogit.server.board.service.boarduser.BoardUserService;
import com.yogit.server.global.dto.ApplicationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/boardusers")
public class BoardUserController {

    private final BoardUserService boardUserService;

    /**
     * 보드 멤버 추가
     * @author 토마스
     */
    @ApiOperation(value = "보드 멤버 추가", notes = "보드 모임 가입 요청")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping
    public ApplicationResponse<BoardUserRes> joinBoardUser(@RequestBody @Validated CreateBoardUserReq dto){
        return boardUserService.joinBoardUser(dto);
    }


    /**
     * 보드 멤버 입장 승인
     * @author 토마스
     */
    @ApiOperation(value = "호스트가 보드 멤버 입장 승인", notes = "호스트가 보드 모임 가입 신청 수락, applyStatus를 변경")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PatchMapping("/applystatus")
    public ApplicationResponse<BoardUserRes> approveBoardUser(@RequestBody @Validated CreateBoardUserReq dto){
        return boardUserService.approveBoardUser(dto);
    }

    /**
     * 보드 멤버 제거
     * @author peanut
     */
    @ApiOperation(value = "보드 멤버 제거", notes = "보드 모임 탈퇴 요청")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PatchMapping
    public ApplicationResponse<Void> delBoardUser(@RequestBody @Validated CreateBoardUserReq dto){
        return boardUserService.delBoardUser(dto);
    }
}
