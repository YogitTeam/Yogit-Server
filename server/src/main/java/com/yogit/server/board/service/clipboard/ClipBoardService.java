package com.yogit.server.board.service.clipboard;

import com.yogit.server.board.dto.request.clipboard.CreateClipBoardReq;
import com.yogit.server.board.dto.request.clipboard.GetAllClipBoardsReq;
import com.yogit.server.board.dto.request.clipboard.GetClipBoardReq;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.List;

public interface ClipBoardService {

    ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq createClipBoardReq);

    ApplicationResponse<GetClipBoardRes> findClipBoard(GetClipBoardReq getClipBoardReq);

    ApplicationResponse<List<GetClipBoardRes>> findAllClipBoards(GetAllClipBoardsReq getAllClipBoardsReq);
}
