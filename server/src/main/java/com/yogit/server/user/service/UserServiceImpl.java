package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.UserEssentialProfileReq;
import com.yogit.server.user.entity.Language;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.repository.LanguageRepository;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public ApplicationResponse<Void> enterEssentialProfile(UserEssentialProfileReq userEssentialProfileReq){

        User user = userRepository.save(userEssentialProfileReq.toEntityUser(userEssentialProfileReq));

        // language 추가
        if(userEssentialProfileReq.getLanguageName1() != null && userEssentialProfileReq.getLanguageLevel1() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(userEssentialProfileReq.getLanguageName1())
                    .level(userEssentialProfileReq.getLanguageLevel1())
                    .build();
            languageRepository.save(language);
        }

        if(userEssentialProfileReq.getLanguageName2() != null && userEssentialProfileReq.getLanguageLevel2() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(userEssentialProfileReq.getLanguageName2())
                    .level(userEssentialProfileReq.getLanguageLevel2())
                    .build();
            languageRepository.save(language);
        }

        if(userEssentialProfileReq.getLanguageName3() != null && userEssentialProfileReq.getLanguageLevel3() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(userEssentialProfileReq.getLanguageName3())
                    .level(userEssentialProfileReq.getLanguageLevel3())
                    .build();
            languageRepository.save(language);
        }

        if(userEssentialProfileReq.getLanguageName4() != null && userEssentialProfileReq.getLanguageLevel4() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(userEssentialProfileReq.getLanguageName4())
                    .level(userEssentialProfileReq.getLanguageLevel4())
                    .build();
            languageRepository.save(language);
        }

        if(userEssentialProfileReq.getLanguageName5() != null && userEssentialProfileReq.getLanguageLevel5() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(userEssentialProfileReq.getLanguageName5())
                    .level(userEssentialProfileReq.getLanguageLevel5())
                    .build();
            languageRepository.save(language);
        }


        return ApplicationResponse.ok();
    }
}
