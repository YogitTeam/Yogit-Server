package com.yogit.server.board.service.boarduser;

import com.yogit.server.board.dto.request.boarduser.CreateBoardUserReq;
import com.yogit.server.board.dto.response.boarduser.BoardUserRes;
import com.yogit.server.global.dto.ApplicationResponse;

public interface BoardUserService {

    ApplicationResponse<BoardUserRes> joinBoardUser(CreateBoardUserReq createBoardUserReq);
}
