package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.*;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageReq;
import com.yogit.server.board.dto.request.boardimage.DeleteBoardImageRes;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.List;

public interface BoardService {

    ApplicationResponse<BoardRes> createBoard(CreateBoardReq createBoardReq);

    ApplicationResponse<BoardRes> updateBoard(PatchBoardReq patchBoardReq);

    ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq deleteBoardReq);

    ApplicationResponse<List<BoardRes>> findAllBoards(GetAllBoardsReq getAllBoardsReq);

    ApplicationResponse<List<BoardRes>> findAllBoardsByCategory(GetAllBoardsByCategoryReq getAllBoardsByCategoryReq);

    ApplicationResponse<BoardRes> findBoard(GetBoardReq getBoardReq);

    ApplicationResponse<DeleteBoardImageRes> deleteBoardImage(DeleteBoardImageReq deleteBoardImageReq);
}
