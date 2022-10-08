package com.yogit.server.board.controller;


import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.service.BoardServiceImpl;
import com.yogit.server.global.dto.ApplicationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor // private final DI의존주입
@RequestMapping("/boards")
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;

    /**
     * 게시글 등록
     * @author 토마스
     */
    @ApiOperation(value = "게시글 등록", notes = "그룹 게시글에 필요한 정보를 입력받아 게시글 생성.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("")
    public ApplicationResponse<BoardRes> registerBoard(@RequestBody @Validated CreateBoardReq createBoardReq){
        return boardServiceImpl.createBoard(createBoardReq);
    }

}
