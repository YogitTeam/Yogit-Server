package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.*;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.List;

public interface BoardService {

    ApplicationResponse<GetBoardRes> createBoard(CreateBoardReq createBoardReq);

    ApplicationResponse<GetBoardRes> updateBoard(PatchBoardReq patchBoardReq);

    ApplicationResponse<DeleteBoardRes> deleteBoard(DeleteBoardReq deleteBoardReq);

    ApplicationResponse<List<List<GetAllBoardRes>>> findAllBoards(GetAllBoardsReq getAllBoardsReq);

    ApplicationResponse<GetAllBoardsByCategoryRes> findAllBoardsByCategory(GetAllBoardsByCategoryReq getAllBoardsByCategoryReq);

    ApplicationResponse<List<List<GetAllBoardRes>>> findBoardsByCategories(GetBoardsByCategoriesReq getBoardsByCategoriesReq);

    ApplicationResponse<GetAllBoardsByCategoryRes> findMyClubBoards(GetMyClubBoardsReq getMyClubBoardsReq);

    ApplicationResponse<GetBoardRes> findBoard(GetBoardReq getBoardReq);

    ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(DeleteBoardImageReq deleteBoardImageReq);
}
