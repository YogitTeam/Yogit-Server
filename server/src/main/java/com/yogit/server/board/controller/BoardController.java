package com.yogit.server.board.controller;


import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.service.BoardService;
import com.yogit.server.global.dto.ApplicationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 등록
     * @author 토마스
     */
    @ApiOperation(value = "게시글 등록", notes = "그룹 게시글에 필요한 정보를 입력받아 게시글 생성.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 City아이디입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Category아이디입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("")
    public ApplicationResponse<BoardRes> registerBoard(@RequestBody @Validated CreateBoardReq createBoardReq){
        return boardService.createBoard(createBoardReq);
    }

    /**
     * 게시글 수정
     * @author 토마스
     */
    @ApiOperation(value = "게시글 수정", notes = "그룹 게시글에 필요한 정보를 입력받아 게시글 수정.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 City아이디입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Category아이디입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code= 404, message = "요청한 유저가 호스트가 아닙니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PatchMapping("")
    public ApplicationResponse<BoardRes> updateBoard(@RequestBody @Validated PatchBoardReq patchBoardReq){
        return boardService.updateBoard(patchBoardReq);
    }

    /**
     * 게시글 삭제
     * @author 토마스
     */
    @ApiOperation(value = "게시글 삭제", notes = "그룹 게시글 삭제 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Board아이디입니다."),
            @ApiResponse(code= 404, message = "요청한 유저가 호스트가 아닙니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PatchMapping("/del")
    public ApplicationResponse<BoardRes> deleteBoard(@RequestBody @Validated DeleteBoardReq deleteBoardReq){
        return boardService.deleteBoard(deleteBoardReq);
    }

    /**
     * 게시글 전체 조회
     * @author 토마스
     */
    @ApiOperation(value = "게시글 전체 조회", notes = "그룹 게시글 전체조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get")
    public ApplicationResponse<List<BoardRes>> findAllBoards(@RequestBody @Validated GetAllBoardsReq getAllBoardsReq){
        return boardService.findAllBoards(getAllBoardsReq);
    }


    /**
     * 게시글 상세 조회
     * @author 토마스
     */
    @ApiOperation(value = "게시글 상세 조회", notes = "게시물 id로 그룹 게시글 상세조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get/detail")
    public ApplicationResponse<BoardRes> findBoard(@RequestBody @Validated GetBoardReq getBoardReq){
        return boardService.findBoard(getBoardReq);
    }
}
