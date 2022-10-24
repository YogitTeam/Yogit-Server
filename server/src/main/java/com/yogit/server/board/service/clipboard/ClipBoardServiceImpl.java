package com.yogit.server.board.service.clipboard;

import com.yogit.server.board.dto.request.clipboard.CreateClipBoardReq;
import com.yogit.server.board.dto.response.clipboard.ClipBoardRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.ClipBoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClipBoardServiceImpl implements ClipBoardService{

    private final ClipBoardRepository clipBoardRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<ClipBoardRes> createClipBoard(CreateClipBoardReq dto){

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // ClipBoard객체 생성
        ClipBoard clipBoard = new ClipBoard(dto, user, board);
        ClipBoard savedClipBoard = clipBoardRepository.save(clipBoard); // 생성 요청
        ClipBoardRes clipBoardRes = ClipBoardRes.toDto(savedClipBoard);// resDto로 변환
        return ApplicationResponse.create("클립보드 생성에 성공하였습니다.", clipBoardRes);
    }
}
