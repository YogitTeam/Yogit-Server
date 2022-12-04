package com.yogit.server.board.controller;


import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.dto.response.GetAllBoardRes;
import com.yogit.server.board.dto.response.GetBoardRes;
import com.yogit.server.board.service.BoardService;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.CityName;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @ApiOperation(value = "게시글 등록", notes = "그룹 게시글에 필요한 정보를 입력받아 게시글 생성. , swagger 에서 이미지(multipartfile)처리가 잘 되지 않으므로, postman으로 테스트 바랍니다. https://solar-desert-882435.postman.co/workspace/Yogit~3e0fe8f2-15e0-41c4-9fcd-b614a975c12a/request/23528495-7fcef771-fae5-486b-a423-b47daf2d0514")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityName", required = true, dataTypeClass = String.class, example = "SEOUL"),
            @ApiImplicitParam(name = "hostId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "title", dataTypeClass = String.class, example = "경복궁 탐사입니다."),
            @ApiImplicitParam(name = "address", dataTypeClass = String.class, example = "서울특별시 종로구 사직로 130"),
            @ApiImplicitParam(name = "addressDetail", dataTypeClass = String.class, example = "강남역 3번 출구"),
            @ApiImplicitParam(name = "longitute", dataTypeClass = float.class, example = "37.1"),
            @ApiImplicitParam(name = "latitude", dataTypeClass = float.class, example = "37.1"),
            @ApiImplicitParam(name = "date", dataTypeClass = LocalDateTime.class, example = "2022-07-13 16:29:30"),
            @ApiImplicitParam(name = "notice", dataTypeClass = String.class, example = "시간에 맞춰오시기 바랍니다."),
            @ApiImplicitParam(name = "introduction", dataTypeClass = String.class, example = "3시에 모여서 경복궁역에서 경복궁으로 출발합니다."),
            @ApiImplicitParam(name = "kindOfPerson", dataTypeClass = String.class, example = "활발한 사람이 오면 좋습니다."),
            @ApiImplicitParam(name = "totalMember", dataTypeClass = int.class, example = "5"),
            @ApiImplicitParam(name = "categoryId", dataTypeClass = Long.class, example = "1"),
    })
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 City아이디입니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 Category아이디입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("")
    public ApplicationResponse<BoardRes> registerBoard(@ModelAttribute @Validated CreateBoardReq createBoardReq){
        return boardService.createBoard(createBoardReq);
    }

    /**
     * 게시글 수정
     * @author 토마스
     */
    @ApiOperation(value = "게시글 수정", notes = "그룹 게시글에 필요한 정보를 입력받아 게시글 수정., swagger 에서 이미지(multipartfile)처리가 잘 되지 않으므로, postman으로 테스트 바랍니다. https://solar-desert-882435.postman.co/workspace/Yogit~3e0fe8f2-15e0-41c4-9fcd-b614a975c12a/request/23528495-1fb37d6c-f31b-4239-bc20-93c3a92e6223")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "cityName", required = true, dataTypeClass = CityName.class, example = "SEOUL"),
            @ApiImplicitParam(name = "hostId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "title", dataTypeClass = String.class, example = "경복궁 탐사입니다."),
            @ApiImplicitParam(name = "address", dataTypeClass = String.class, example = "서울특별시 종로구 사직로 130"),
            @ApiImplicitParam(name = "longitute", dataTypeClass = float.class, example = "37.1"),
            @ApiImplicitParam(name = "latitude", dataTypeClass = float.class, example = "37.1"),
            @ApiImplicitParam(name = "date", dataTypeClass = LocalDateTime.class, example = "2022-07-13 16:29:30"),
            @ApiImplicitParam(name = "notice", dataTypeClass = String.class, example = "시간에 맞춰오시기 바랍니다."),
            @ApiImplicitParam(name = "introduction", dataTypeClass = String.class, example = "3시에 모여서 경복궁역에서 경복궁으로 출발합니다."),
            @ApiImplicitParam(name = "kindOfPerson", dataTypeClass = String.class, example = "활발한 사람이 오면 좋습니다."),
            @ApiImplicitParam(name = "totalMember", dataTypeClass = int.class, example = "5"),
            @ApiImplicitParam(name = "categoryId", dataTypeClass = Long.class, example = "1"),
    })
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
    public ApplicationResponse<BoardRes> updateBoard(@ModelAttribute @Validated PatchBoardReq patchBoardReq){
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
    @PatchMapping("/status")
    public ApplicationResponse<BoardRes> deleteBoard(@RequestBody @Validated DeleteBoardReq deleteBoardReq){
        return boardService.deleteBoard(deleteBoardReq);
    }

    /**
     * 게시글 전체 조회: 게시글 메인 화면
     * board category 별 리스트 조회 (15개씩)
     * @author 토마스
     */
    @ApiOperation(value = "게시글 전체 조회: 게시글 메인 화면", notes = "그룹 게시글 전체조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get")
    public ApplicationResponse<List<List<GetAllBoardRes>>> findAllBoards(@RequestBody @Validated GetAllBoardsReq getAllBoardsReq){
        return boardService.findAllBoards(getAllBoardsReq);
    }


    /**
     * 게시글 카테고리 별 전체 조회
     * @author 토마스
     */
    @ApiOperation(value = "게시글 카테고리 별 전체 조회", notes = "그룹 게시글 카테고리 별 전체 조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get/category")
    public ApplicationResponse<List<GetAllBoardRes>> findAllBoardsByCategory(@RequestBody @Validated GetAllBoardsByCategoryReq getAllBoardsByCategoryReq){
        return boardService.findAllBoardsByCategory(getAllBoardsByCategoryReq);
    }


    /**
     * 게시글 모든 카테코리 별 리스트 모음 조회
     * @author 토마스
     */
    @ApiOperation(value = "게시글 모든 카테코리 별 리스트 모음 조회", notes = "그룹 게시글 모든 카테코리 별 리스트 모음 조회")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get/categories")
    public ApplicationResponse<List<List<GetAllBoardRes>>> findBoardsByCategories(@RequestBody @Validated GetBoardsByCategoriesReq dto){
        return boardService.findBoardsByCategories(dto);
    }

    /**
     * 게시글 My Club 조회: 자신이 생성한 보드들 조회
     * @author 토마스
     */
    @ApiOperation(value = "게시글 My Club 조회: 자신이 생성한 보드들 조회", notes = "그룹 게시글 전체조회 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PostMapping("/get/myclub")
    public ApplicationResponse<List<GetAllBoardRes>> findMyClubBoards(@RequestBody @Validated GetAllBoardsReq dto){
        return boardService.findMyClubBoards(dto);
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
    public ApplicationResponse<GetBoardRes> findBoard(@RequestBody @Validated GetBoardReq getBoardReq){
        return boardService.findBoard(getBoardReq);
    }


    /**
     * 게시글 이미지 삭제
     * @author 토마스
     */
    @ApiOperation(value = "게시글 이미지 삭제", notes = "게시물 이미지 url을 입력해 이미지 삭제 요청.")
    @ApiResponses({
            @ApiResponse(code= 201, message = "요청에 성공하였습니다."),
            @ApiResponse(code= 404, message = "존재하지 않는 유저입니다."),
            @ApiResponse(code = 4000 , message =  "서버 오류입니다.")
    })
    @PatchMapping("/boardimage")
    public ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(@RequestBody @Validated DeleteBoardImageReq deleteBoardImageReq){
        return boardService.deleteBoardImage(deleteBoardImageReq);
    }
}
