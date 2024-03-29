package com.yogit.server.block.service;

import com.yogit.server.block.dto.req.CreateBlockReq;
import com.yogit.server.block.dto.res.BlockRes;
import com.yogit.server.block.entity.Block;
import com.yogit.server.block.exception.AlreadyBlockingException;
import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.BoardUser;
import com.yogit.server.board.repository.BoardRepository;
import com.yogit.server.board.repository.BoardUserRepository;
import com.yogit.server.config.domain.BaseStatus;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.global.service.TokenService;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TokenService tokenService;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<BlockRes> createBlock(CreateBlockReq dto){

        tokenService.validateRefreshToken(dto.getBlockingUserId(), dto.getRefreshToken());

        // 차단 생성하는 유저 조회
        User blockingUser = userRepository.findByUserId(dto.getBlockingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        // 차단 받는 유저 조회
        User blockedUser = userRepository.findByUserId(dto.getBlockedUserId())
                .orElseThrow(() -> new NotFoundUserException());

        //Validation: 중복 차단(이미 차단했는지 검증)
        //TODO: 1.차단 리파지토리로 조회 or 2.유저의 차단 리스트 참조 조회 중 좋은 방법 선택해야 됨.
        if (!blockRepository.findByBlockingUserIdAndBlockedUserId(dto.getBlockingUserId(), dto.getBlockedUserId()).isEmpty()) {
            throw new AlreadyBlockingException();
        }

        // 차단 엔티티 새성, 저장
        Block block = new Block(blockingUser, blockedUser);
        blockRepository.save(block);

        //차단후 연관 보드 처리
        //1)차단당한 유저가 호스트일 때-차단신청한 유저의 보드유저 삭제
        //차단당한 유저가 호스트이고, 자신이 참여한 보드 조회
        //차단신청한 유저의 보드유저 삭제
        List<Board> boardsOfBlockedUser = boardRepository.findBoardsByUserId(blockedUser.getId());
        if(!boardsOfBlockedUser.isEmpty()){
            for(Board b:boardsOfBlockedUser){
                List<BoardUser> boardUsers = b.getBoardUsers();
                if(!boardUsers.isEmpty()){
                    for(BoardUser bu: boardUsers){
                        if(bu.getUser().getId().equals(blockingUser.getId()) && bu.getStatus().equals(BaseStatus.ACTIVE)){
                            bu.changeStatusToInactive();
                        }
                    }
                }
            }
        }

        //2)차단당한 사람이 보드 멤버일 때-차단 당한 사람의 보드유저 삭제
        //내가 호스트이고 차단당한 유저가 멤버인 보드 조회
        //차단 당한 사람의 보드유저 삭제
        List<Board> boardsOfBlockingUser = boardRepository.findBoardsByUserId(blockingUser.getId());
        if(!boardsOfBlockingUser.isEmpty()){
            for(Board b: boardsOfBlockingUser){
                List<BoardUser> boardUsers = b.getBoardUsers();
                if(!boardUsers.isEmpty()){
                    for(BoardUser bu: boardUsers){
                        if(bu.getUser().getId().equals(blockedUser.getId()) && bu.getStatus().equals(BaseStatus.ACTIVE)){
                            bu.changeStatusToInactive();
                        }
                    }
                }
            }
        }

        BlockRes res = BlockRes.toDto(block);
        return ApplicationResponse.create("자단하였습니다.", res);
    }

    @Override
    public boolean isBlockingUser(Long blockingUserId, Long blockedUserId){
        return blockRepository.existsByBlockingUserIdAndBlockedUserId(blockingUserId, blockedUserId);
    }
}
