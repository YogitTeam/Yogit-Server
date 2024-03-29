package com.yogit.server.block.service;

import com.yogit.server.block.dto.req.CreateBlockReq;
import com.yogit.server.block.dto.res.BlockRes;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.entity.User;

public interface BlockService {

    ApplicationResponse<BlockRes> createBlock(CreateBlockReq createBlockReq);
    boolean isBlockingUser(Long blockingUserId, Long blockedUserId);
}
