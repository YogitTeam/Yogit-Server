package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.entity.*;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.exception.UserDuplicationLoginId;
import com.yogit.server.user.repository.*;
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
    private final UserImageRepository userImageRepository;
    private final CityRepository cityRepository;
    private final InterestRepository interestRepository;
    private final UserInterestRepository userInterestRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    @Override
    public ApplicationResponse<UserProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq){

        User user = userRepository.findById(createUserEssentialProfileReq.getUserId()).orElseThrow();

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
    public ApplicationResponse<UserProfileRes> editEssentialProfile(EditUserEssentialProfileReq editUserEssentialProfileReq){

        User user = userRepository.findById(editUserEssentialProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.changeUserInfo(editUserEssentialProfileReq);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        if(editUserEssentialProfileReq.getLanguageName1() != null){
            // 기존 language 들 삭제
            languageRepository.deleteAllByUserId(user.getId());

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

    @Override
    @Transactional
    public ApplicationResponse<Void> delProfile(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);

        // 유저 탈퇴시 이름, 대표 사진 null 처리
        user.delUser();

        // image 엔티티 삭제
        userImageRepository.deleteAllByUserId(user.getId());

        return ApplicationResponse.ok();
    }

    @Override
    @Transactional
    public ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq){

        User user = userRepository.findById(createUserImageReq.getUserId()).orElseThrow(NotFoundUserException::new);
        UserImagesRes userImagesRes = new UserImagesRes();

        // 메인 프로필 사진 업로드
        String mainImageUUid = awsS3Service.uploadImage(createUserImageReq.getProfileImage());
        user.changeMainImgUUid(mainImageUUid);
        userImagesRes.setProfileImageUrl(awsS3Service.makeUrlOfFilename(mainImageUUid));

        // 나머지 사진 업로드
        List<String> imageUUids = awsS3Service.uploadImages(createUserImageReq.getImages());
        for(String i : imageUUids){
            UserImage userImage = createUserImageReq.toEntityUserImage(user, i);
            userImageRepository.save(userImage);
            userImagesRes.addImage(awsS3Service.makeUrlOfFilename(userImage.getImgUUid()));
        }

        return ApplicationResponse.ok(userImagesRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq){
        User user = userRepository.findById(addUserAdditionalProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);

        user.addAdditionalProfile(addUserAdditionalProfileReq.getLatitude(), addUserAdditionalProfileReq.getLongitude(), addUserAdditionalProfileReq.getAboutMe());

        UserAdditionalProfileRes userAdditionalProfileRes = UserAdditionalProfileRes.create(user);

        if(addUserAdditionalProfileReq.getCity() != null){
            City city = City.builder()
                    .user(user)
                    .name(addUserAdditionalProfileReq.getCity())
                    .build();
            cityRepository.save(city);

            userAdditionalProfileRes.setCity(city.getName());
        }

        for(String interestName : addUserAdditionalProfileReq.getInterests()){
            Interest interest = Interest.builder()
                    .name(interestName)
                    .build();
            interestRepository.save(interest);

            UserInterest userInterest = UserInterest.builder()
                    .user(user)
                    .interest(interest)
                    .build();
            userInterestRepository.save(userInterest);

            userAdditionalProfileRes.getInterests().add(interestName);
        }

        return ApplicationResponse.ok(userAdditionalProfileRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<Void> createUser(CreateUserReq createUserReq){

        if(userRepository.existsByLoginId(createUserReq.getLoginId())) throw new UserDuplicationLoginId();

        userRepository.save(createUserReq.toEntityUser(createUserReq));

        return ApplicationResponse.ok();
    }
}
