package com.yogit.server.user.service;

import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static UserRepository userRepository;

    public void enterEssentialProfile(UserEssentialProfileReq userEssentialProfileReq){
        userRepository.save(userEssentialProfileReq.toEntity(userEssentialProfileReq));

        // language 추가
    }
}
