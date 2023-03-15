package com.yogit.server.global.service;

import com.yogit.server.applelogin.exception.InvalidRefreshTokenException;
import com.yogit.server.applelogin.exception.NotFoundRefreshTokenException;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final UserRepository userRepository;

    /**
     * 리프레시 토큰 검증
     *
     * refresh_token은 만료되지 않기 때문에 권한이 필요한 요청일 경우
     * 굳이 매번 애플 ID 서버로부터 refresh_token을 통해 access_token을 발급 받기보다는
     * 유저의 refresh_token을 따로 DB나 기타 저장소에 저장해두고 캐싱해두고 조회해서 검증하는편이 성능면에서 낫다는 자료를 참고
     * https://hwannny.tistory.com/71
     */
    public Void validateRefreshToken(Long userId, String refreshToken){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundUserException());

        if(user.getRefreshToken() == null) throw new NotFoundRefreshTokenException();

        if(!user.getRefreshToken().equals(refreshToken)) throw new InvalidRefreshTokenException();

        return null;
    }
}
