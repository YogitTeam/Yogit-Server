package com.yogit.server.block.service;

import com.yogit.server.block.dto.req.CreateBlockReq;
import com.yogit.server.block.dto.res.BlockRes;
import com.yogit.server.block.entity.Block;
import com.yogit.server.block.exception.AlreadyBlockingException;
import com.yogit.server.block.repository.BlockRepository;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import com.yogit.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<BlockRes> createBlock(CreateBlockReq dto){

        userService.validateRefreshToken(dto.getBlockingUserId(), dto.getRefreshToken());

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

        BlockRes res = BlockRes.toDto(block);
        return ApplicationResponse.create("자단하였습니다.", res);
    }
}
