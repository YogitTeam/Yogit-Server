package com.yogit.server.board.service;

import com.yogit.server.board.dto.request.CreateBoardReq;
import com.yogit.server.board.dto.response.BoardRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardRes> createBoard(CreateBoardReq dto){

        // host 조회
        // city조회
        // boardUsers 조회
        // boardImages 조회
        // boardCategories 조회

        // board 생성
        Board board = dto.toEntity(dto);
        // currentMember 디폴트=0
        board.changeBoardCurrentMember(0);

        // board 생성 요청
        Board savedBoard = boardRepository.save(board);
        // resDto 벼환
        BoardRes boardRes = BoardRes.toDto(savedBoard);
        // 반환
        return ApplicationResponse.create("created", boardRes);
    }

}
