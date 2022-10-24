package com.yogit.server.board.service.clipboard;

import com.yogit.server.board.dto.request.clipboard.CreateClipBoardReq;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

public interface ClipBoardService {

    ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq createClipBoardReq);
}
