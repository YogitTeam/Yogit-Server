package com.yogit.server.block.service;

import com.yogit.server.block.dto.req.CreateBlockReq;
import com.yogit.server.block.dto.res.BlockRes;
import com.yogit.server.block.entity.Block;
import com.yogit.server.block.repository.BlockRepository;
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
public class BlockServiceImpl implements BlockService{

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public ApplicationResponse<BlockRes> createBlock(CreateBlockReq dto){

        // 차단 생성하는 유저 조회
        User blockingUser = userRepository.findById(dto.getBlockingUserId())
                .orElseThrow(() -> new NotFoundUserException());
        // 차단 받는 유저 조회
        User blockedUser = userRepository.findById(dto.getBlockedUserId())
                .orElseThrow(() -> new NotFoundUserException());

        //Validation: 중복 차단(이미 차단했는지 검증)

        // 차단 엔티티 새성, 저장
        Block block = new Block(blockingUser, blockedUser);
        blockRepository.save(block);

        BlockRes res = BlockRes.toDto(block);
        return ApplicationResponse.create("자단하였습니다.", res);
    }
}
