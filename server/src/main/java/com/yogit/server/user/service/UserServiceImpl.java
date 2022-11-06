package com.yogit.server.user.service;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.dto.request.AddUserAdditionalProfileReq;
import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.dto.request.CreateUserImageReq;
import com.yogit.server.user.dto.request.CreateUserReq;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserEssentialProfileRes;
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
import java.util.Optional;

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
    public ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq){

        User user = userRepository.findById(createUserEssentialProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.changeUserInfo(createUserEssentialProfileReq.getUserName(), createUserEssentialProfileReq.getUserAge(), createUserEssentialProfileReq.getGender(), createUserEssentialProfileReq.getNationality());

        UserEssentialProfileRes userEssentialProfileRes = UserEssentialProfileRes.create(createUserEssentialProfileReq.getUserId(), createUserEssentialProfileReq.getUserName(), createUserEssentialProfileReq.getUserAge(), createUserEssentialProfileReq.getGender(), createUserEssentialProfileReq.getNationality());

        if(!createUserEssentialProfileReq.getLanguageNames().isEmpty()) {
            // 기존 languages 삭제
            languageRepository.deleteAllByUserId(createUserEssentialProfileReq.getUserId());
            // 새로운 languages 추가
            for(int i=0;i < createUserEssentialProfileReq.getLanguageNames().size(); i++){
                Language language = Language.builder()
                        .user(user)
                        .name(createUserEssentialProfileReq.getLanguageNames().get(i))
                        .level(createUserEssentialProfileReq.getLanguageLevels().get(i))
                        .build();
                languageRepository.save(language);

                userEssentialProfileRes.addLanguage(createUserEssentialProfileReq.getLanguageNames().get(i), createUserEssentialProfileReq.getLanguageLevels().get(i));
            }
        }

        return ApplicationResponse.ok(userEssentialProfileRes);
    }


    @Override
    public ApplicationResponse<UserProfileRes> getProfile(Long userId){
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        // if(user.getCity())  userProfileRes.addCity(user.getCity().getName()); // TODO administrativeArea 와의 차이 문의중 (활동 지역 필요하면, 지금 city 연관관계로 진행)

        List<Language> languages = languageRepository.findAllByUserId(userId);
        if(!languages.isEmpty()){
            for(Language l : languages){
                userProfileRes.addLanguage(l.getName(), l.getLevel());
            }
        }

        Optional<UserInterest> userInterest = userInterestRepository.findByUserId(userId);
        if(userInterest.isPresent()){
            userProfileRes.addInterest(userInterest.get().getInterest().getName());
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

        userRepository.save(CreateUserReq.toEntityUser(createUserReq));

        return ApplicationResponse.ok();
    }
}
