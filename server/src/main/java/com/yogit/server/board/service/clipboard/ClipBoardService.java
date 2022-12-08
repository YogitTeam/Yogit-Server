package com.yogit.server.board.service.clipboard;

import com.yogit.server.board.dto.request.clipboard.*;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.dto.response.clipboard.GetClipBoardRes;
import com.yogit.server.global.dto.ApplicationResponse;

import java.util.List;

public interface ClipBoardService {

    ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq createClipBoardReq);

    ApplicationResponse<GetClipBoardRes> findClipBoard(GetClipBoardReq getClipBoardReq);

    ApplicationResponse<List<GetClipBoardRes>> findAllClipBoards(GetAllClipBoardsReq getAllClipBoardsReq);

    ApplicationResponse<String> deleteClipBoard(DeleteClipBoardReq deleteClipBoardReq);

    ApplicationResponse<ClipBoardRes> updateClipBoard(PatchClipBoardReq patchClipBoardReq);
}
