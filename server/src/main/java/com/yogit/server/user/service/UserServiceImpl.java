package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.createUserEssentialProfileReq;
import com.yogit.server.user.dto.request.editUserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.entity.Language;
import com.yogit.server.user.entity.User;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.repository.LanguageRepository;
import com.yogit.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

    @Transactional
    @Override
    public ApplicationResponse<UserProfileRes> enterEssentialProfile(createUserEssentialProfileReq createUserEssentialProfileReq){

        User user = userRepository.save(createUserEssentialProfileReq.toEntityUser(createUserEssentialProfileReq));

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        // language 추가
        if(createUserEssentialProfileReq.getLanguageName1() != null && createUserEssentialProfileReq.getLanguageLevel1() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(createUserEssentialProfileReq.getLanguageName1())
                    .level(createUserEssentialProfileReq.getLanguageLevel1())
                    .build();
            languageRepository.save(language);
            userProfileRes.addLanguage(language);
        }

        if(createUserEssentialProfileReq.getLanguageName2() != null && createUserEssentialProfileReq.getLanguageLevel2() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(createUserEssentialProfileReq.getLanguageName2())
                    .level(createUserEssentialProfileReq.getLanguageLevel2())
                    .build();
            languageRepository.save(language);
            userProfileRes.addLanguage(language);
        }

        if(createUserEssentialProfileReq.getLanguageName3() != null && createUserEssentialProfileReq.getLanguageLevel3() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(createUserEssentialProfileReq.getLanguageName3())
                    .level(createUserEssentialProfileReq.getLanguageLevel3())
                    .build();
            languageRepository.save(language);
            userProfileRes.addLanguage(language);
        }

        if(createUserEssentialProfileReq.getLanguageName4() != null && createUserEssentialProfileReq.getLanguageLevel4() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(createUserEssentialProfileReq.getLanguageName4())
                    .level(createUserEssentialProfileReq.getLanguageLevel4())
                    .build();
            languageRepository.save(language);
            userProfileRes.addLanguage(language);
        }

        if(createUserEssentialProfileReq.getLanguageName5() != null && createUserEssentialProfileReq.getLanguageLevel5() != null){
            Language language = Language.builder()
                    .user(user)
                    .name(createUserEssentialProfileReq.getLanguageName5())
                    .level(createUserEssentialProfileReq.getLanguageLevel5())
                    .build();
            languageRepository.save(language);
            userProfileRes.addLanguage(language);
        }

        return ApplicationResponse.create("created", userProfileRes);
    }

    @Transactional
    @Override
    public ApplicationResponse<UserProfileRes> editEssentialProfile(editUserEssentialProfileReq editUserEssentialProfileReq){

        User user = userRepository.findById(editUserEssentialProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.changeUserInfo(editUserEssentialProfileReq);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        if(editUserEssentialProfileReq.getLanguageName1() != null){
            // 기존 language 들 삭제
            List<Language> languages = languageRepository.findAllByUserId(user.getId());
            for(Language l : languages){
                languageRepository.deleteById(l.getId());
            }

            // language 추가
            if(editUserEssentialProfileReq.getLanguageName1() != null && editUserEssentialProfileReq.getLanguageLevel1() != null){
                Language language = Language.builder()
                        .user(user)
                        .name(editUserEssentialProfileReq.getLanguageName1())
                        .level(editUserEssentialProfileReq.getLanguageLevel1())
                        .build();
                languageRepository.save(language);
                userProfileRes.addLanguage(language);
            }

            if(editUserEssentialProfileReq.getLanguageName2() != null && editUserEssentialProfileReq.getLanguageLevel2() != null){
                Language language = Language.builder()
                        .user(user)
                        .name(editUserEssentialProfileReq.getLanguageName2())
                        .level(editUserEssentialProfileReq.getLanguageLevel2())
                        .build();
                languageRepository.save(language);
                userProfileRes.addLanguage(language);
            }

            if(editUserEssentialProfileReq.getLanguageName3() != null && editUserEssentialProfileReq.getLanguageLevel3() != null){
                Language language = Language.builder()
                        .user(user)
                        .name(editUserEssentialProfileReq.getLanguageName3())
                        .level(editUserEssentialProfileReq.getLanguageLevel3())
                        .build();
                languageRepository.save(language);
                userProfileRes.addLanguage(language);
            }

            if(editUserEssentialProfileReq.getLanguageName4() != null && editUserEssentialProfileReq.getLanguageLevel4() != null){
                Language language = Language.builder()
                        .user(user)
                        .name(editUserEssentialProfileReq.getLanguageName4())
                        .level(editUserEssentialProfileReq.getLanguageLevel4())
                        .build();
                languageRepository.save(language);
                userProfileRes.addLanguage(language);
            }

            if(editUserEssentialProfileReq.getLanguageName5() != null && editUserEssentialProfileReq.getLanguageLevel5() != null){
                Language language = Language.builder()
                        .user(user)
                        .name(editUserEssentialProfileReq.getLanguageName5())
                        .level(editUserEssentialProfileReq.getLanguageLevel5())
                        .build();
                languageRepository.save(language);
                userProfileRes.addLanguage(language);
            }
        }

        return ApplicationResponse.ok(userProfileRes);
    }

    @Override
    public ApplicationResponse<UserProfileRes> getProfile(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        List<Language> languages = languageRepository.findAllByUserId(userId);
        if(!languages.isEmpty()){
            for(Language l : languages){
                userProfileRes.addLanguage(l);
            }
        }

        return ApplicationResponse.ok(userProfileRes);
    }
}
