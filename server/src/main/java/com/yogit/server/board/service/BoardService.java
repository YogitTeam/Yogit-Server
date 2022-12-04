package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.dto.response.GetAllBoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.List;

public interface BoardService {

    ApplicationResponse<BoardRes> createBoard(CreateBoardReq createBoardReq);

    ApplicationResponse<BoardRes> updateBoard(PatchBoardReq patchBoardReq);

    ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq deleteBoardReq);

    ApplicationResponse<List<List<GetAllBoardRes>>> findAllBoards(GetAllBoardsReq getAllBoardsReq);

    ApplicationResponse<List<GetAllBoardRes>> findAllBoardsByCategory(GetAllBoardsByCategoryReq getAllBoardsByCategoryReq);

    ApplicationResponse<List<List<GetAllBoardRes>>> findBoardsByCategories(GetBoardsByCategoriesReq getBoardsByCategoriesReq);

    ApplicationResponse<List<GetAllBoardRes>> findMyClubBoards(GetAllBoardsReq getAllBoardsReq);

    ApplicationResponse<BoardRes> findBoard(GetBoardReq getBoardReq);

    ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(DeleteBoardImageReq deleteBoardImageReq);
}
