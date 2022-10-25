package com.yogit.server.board.controller;

import com.yogit.server.board.dto.request.clipboard.CreateClipBoardReq;
import com.yogit.server.board.dto.request.clipboard.GetAllClipBoardsReq;
import com.yogit.server.board.dto.request.clipboard.GetClipBoardReq;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.board.service.clipboard.ClipBoardService;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/clipboards")
public class ClipBoardController {

    private final ClipBoardService clipBoardService;

    /**
     * 클립보드 등록
     * @author 토마스
     */
    @ApiOperation(value = "클립보드 등록", notes = "클리보드 제목과, 내용으로 등록 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("")
    public ApplicationResponse<ClipBoardRes> createClipBoard(@RequestBody @Validated CreateClipBoardReq createClipBoardReq){
        return clipBoardService.createClipBoard(createClipBoardReq);
    }

    /**
     * 클립보드 단건 조회
     * @author 토마스
     */
    @ApiOperation(value = "클립보드 단건 조회", notes = "클립보드 ID로 단건 조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get")
    public ApplicationResponse<GetClipBoardRes> findClipBoard(@RequestBody @Validated GetClipBoardReq getClipBoardReq){
        return clipBoardService.findClipBoard(getClipBoardReq);
    }


    /**
     * 클립보드 전체 조회
     * @author 토마스
     */
    @ApiOperation(value = "클립보드 전체 조회", notes = "Board ID로 클립보드 전체 조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get/all")
    public ApplicationResponse<List<ClipBoardRes>> findAllClipBoards(@RequestBody @Validated GetAllClipBoardsReq getAllClipBoardsReq){
        return clipBoardService.findAllClipBoards(getAllClipBoardsReq);
    }
}
