package com.yogit.server.board.service.boarduser;

import com.yogit.server.apns.dto.req.CreateBoardUserJoinAPNReq;
import com.yogit.server.apns.service.APNService;
import com.yogit.server.board.dto.request.boarduser.CreateBoardUserReq;
import com.yogit.server.board.dto.response.boarduser.BoardUserRes;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.exception.DuplicatedBoardUserException;
import com.yogit.server.board.exception.MaxBoardUserException;
import com.yogit.server.board.exception.NotFoundBoardException;
import com.yogit.server.board.exception.NotFoundUserBoard;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.BoardUserRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.global.service.TokenService;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.entity.UserStatus;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardUserServiceImpl implements BoardUserService{

    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final APNService apnService;
    private final TokenService tokenService;

    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardUserRes> joinBoardUser(CreateBoardUserReq dto) {

        tokenService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
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

        //board.addCurrentMember();// 보드 현재 인원 +1
        board.addBoardUser(boardUser); // 보드에 멤버 추가

        List<User> participants = board.getBoardUsers().stream()
                //.filter(bu -> !bu.getUser().equals(board.getHost()))
                .filter(bu -> bu.getApplyStatus().equals(1)) // 참여 승인된 사람만 조회
                .map(bu -> bu.getUser())
                .collect(Collectors.toList());

        List<String> participantsImageUUIds = board.getBoardUsers().stream()
                //.filter(bu -> !bu.getUser().equals(board.getHost()))
                .filter(bu -> bu.getApplyStatus().equals(1)) // 참여 승인된 사람만 조회
                .map(bu -> bu.getUser().getProfileImg())
                .collect(Collectors.toList());

        // 호스트에게 참여 APN 푸쉬 알림

        try {
            if(user.getUserStatus().equals(UserStatus.LOGIN)) apnService.createBoardUserJoinAPN(new CreateBoardUserJoinAPNReq(board.getHost().getDeviceToken(), user.getName(), board.getId(), board.getTitle()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BoardUserRes res = BoardUserRes.toDto(boardUser,user, board, participants, awsS3Service.makeUrlsOfFilenames(participantsImageUUIds));
        return ApplicationResponse.create("보드에 유저가 조인되었습니다.", res);
    }


    @Transactional(readOnly = false)
    @Override
    public ApplicationResponse<BoardUserRes> approveBoardUser(CreateBoardUserReq dto){

        tokenService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        BoardUser boardUser = boardUserRepository.findByUserIdAndBoardId(user.getId(), board.getId())
                .orElseThrow(() -> new NotFoundUserBoard());

        // Validation: 보드 인원 다 차면 신청 불가능 검증
        if(board.getCurrentMember() >= board.getTotalMember()){
            throw new MaxBoardUserException();
        }

        boardUser.changeApplyStatus(); // 참여 승인으로 상태 업데이트
        board.addCurrentMember();// 보드 현재 인원 +1
        //board.addBoardUser(boardUser); // 보드에 멤버 추가

        List<User> participants = board.getBoardUsers().stream()
                //.filter(bu -> !bu.getUser().equals(board.getHost()))
                .filter(bu -> bu.getApplyStatus().equals(1)) // 참여 승인된 사람만 조회
                .map(bu -> bu.getUser())
                .collect(Collectors.toList());

        List<String> participantsImageUUIds = board.getBoardUsers().stream()
                //.filter(bu -> !bu.getUser().equals(board.getHost()))
                .filter(bu -> bu.getApplyStatus().equals(1)) // 참여 승인된 사람만 조회
                .map(bu -> bu.getUser().getProfileImg())
                .collect(Collectors.toList());

        BoardUserRes res = BoardUserRes.toDto(boardUser,user, board, participants, awsS3Service.makeUrlsOfFilenames(participantsImageUUIds));
        return ApplicationResponse.create("보드에 유저가 조인되었습니다.", res);
    }

    @Transactional
    @Override
    public ApplicationResponse<Void> delBoardUser(CreateBoardUserReq dto){

        tokenService.validateRefreshToken(dto.getUserId(), dto.getRefreshToken());

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        Board board = boardRepository.findBoardById(dto.getBoardId())
                .orElseThrow(() -> new NotFoundBoardException());

        Optional<BoardUser> boardUser =  boardUserRepository.findByUserIdAndBoardId(dto.getUserId(), dto.getBoardId());

        if(!boardUser.isPresent()) throw new NotFoundUserBoard();

        boardUserRepository.deleteById(boardUser.get().getId());

        return ApplicationResponse.ok();
    }
}
