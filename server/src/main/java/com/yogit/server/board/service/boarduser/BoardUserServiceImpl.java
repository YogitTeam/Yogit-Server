package com.yogit.server.board.service.boarduser;

import com.yogit.server.board.dto.request.boarduser.CreateBoardUserReq;
import com.yogit.server.board.dto.response.boarduser.BoardUserRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.exception.DuplicatedBoardUserException;
import com.yogit.server.board.exception.MaxBoardUserException;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.BoardUserRepository;
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
public class BoardUserServiceImpl implements BoardUserService{

    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardUserRes> joinBoardUser(CreateBoardUserReq dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        // Validation: 유저가 멤버에 이미 가입되어 있는지 검증
        if(boardUserRepository.findByUserIdAndBoardId(dto.getUserId(), dto.getBoardId()).isPresent()){
            throw new DuplicatedBoardUserException();
        }

        // Validation: 보드 인원 다 차면 신청 불가능 검증
        if(board.getCurrentMember() >= board.getTotalMember()){
            throw new MaxBoardUserException();
        }

        BoardUser boardUser = new BoardUser(user, board);
        BoardUser savedBoardUser = boardUserRepository.save(boardUser);

        board.addCurrentMember();// 보드 현재 인원 +1
        board.addBoardUser(boardUser); // 보드에 멤버 추가

        BoardUserRes res = BoardUserRes.toDto(boardUser,user, board);
        return ApplicationResponse.create("보드에 유저가 조인되었습니다.", res);
    }
}
