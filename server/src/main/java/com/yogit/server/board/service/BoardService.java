package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.request.DeleteBoardReq;
import com.yogit.server.board.dto.request.PatchBoardReq;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

public interface BoardService {

    ApplicationResponse<BoardRes> createBoard(CreateBoardReq createBoardReq);

    ApplicationResponse<BoardRes> updateBoard(PatchBoardReq patchBoardReq);

    ApplicationResponse<BoardRes> deleteBoard(DeleteBoardReq deleteBoardReq);
}
