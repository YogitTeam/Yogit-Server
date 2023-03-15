package com.yogit.server.user.service;

import com.yogit.server.applelogin.exception.InvalidRefreshTokenException;
import com.yogit.server.applelogin.exception.NotFoundRefreshTokenException;
import com.yogit.server.block.service.BlockService;
import com.yogit.server.config.domain.BaseStatus;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.global.service.TokenService;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.LogoutRes;
import com.yogit.server.user.dto.response.UserDeviceTokenRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.entity.*;
import com.yogit.server.user.exception.NotFoundUserException;
import com.yogit.server.user.exception.NotFoundUserImageException;
import com.yogit.server.user.exception.UserDuplicationLoginId;
import com.yogit.server.user.exception.UserGenderException;
import com.yogit.server.user.repository.*;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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
    private final BlockService blockService;
    private final TokenService tokenService;

    // 국가 정보 조회 Open Api
    JSONObject getNation(String nationality) {

        JSONObject nation = null;
        try {
            URL url = new URL("http://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?ServiceKey=Os%2B%2Fa%2BWGJPptb5Rf1U850JQo11XO0fCA5cL3YND%2BxoxUm8B38IDZjHKlrpV0gj496%2Br53Rg61EdzI9KDuILDrg%3D%3D" + "&cond[country_iso_alp2::EQ]=" + nationality);

            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray data = (JSONArray) jsonObject.get("data");
            nation = (JSONObject) data.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nation;
    }

    @Transactional
    @Override
    public ApplicationResponse<UserProfileRes> enterProfile(CreateUserProfileReq createUserProfileReq){

        tokenService.validateRefreshToken(createUserProfileReq.getUserId(), createUserProfileReq.getRefreshToken());

        if(createUserProfileReq.getGender() != null && !createUserProfileReq.getGender().equals("Prefer not to say") && !createUserProfileReq.getGender().equals("Male") && !createUserProfileReq.getGender().equals("Female")) throw new UserGenderException();

        User user = userRepository.findByUserId(createUserProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.changeUserInfo(createUserProfileReq.getUserName(), createUserProfileReq.getUserAge(), createUserProfileReq.getGender(), createUserProfileReq.getNationality());

        UserProfileRes userProfileRes = UserProfileRes.create(user);
        if(createUserProfileReq.getAboutMe() != null)userProfileRes.setAboutMe(createUserProfileReq.getAboutMe());
        if(createUserProfileReq.getJob() != null)userProfileRes.setJob(createUserProfileReq.getJob());

        if(createUserProfileReq.getLanguageCodes() != null) {
            // 기존 languages 삭제
            languageRepository.deleteAllByUserId(createUserProfileReq.getUserId());
            // 새로운 languages 추가
            for(int i=0;i < createUserProfileReq.getLanguageCodes().size(); i++){
                Language language = Language.builder()
                        .user(user)
                        .code(createUserProfileReq.getLanguageCodes().get(i))
                        .level(createUserProfileReq.getLanguageLevels().get(i))
                        .build();
                languageRepository.save(language);

                userProfileRes.addLanguage(createUserProfileReq.getLanguageCodes().get(i), createUserProfileReq.getLanguageLevels().get(i));
            }
        }

        user.addAdditionalProfile(createUserProfileReq.getLatitude(), createUserProfileReq.getLongitude(), createUserProfileReq.getAboutMe(), createUserProfileReq.getJob());

        // 기존에 존재하는 city인 경우
        if(cityRepository.existsByCityName(createUserProfileReq.getCityName())){
            City city = cityRepository.findByCityName(createUserProfileReq.getCityName());
            city.addUser(user);
        }
        else{ // 기존에 존재하지 않는 city인 경우
            City city = City.builder()
                    .user(user)
                    .cityName(createUserProfileReq.getCityName())
                    .build();
            cityRepository.save(city);
            city.addUser(user);
        }
        userProfileRes.setCity(createUserProfileReq.getCityName());

        // 관심사 등록
        for(String interestName : createUserProfileReq.getInterests()){
            // 기존에 존재하는 관심사일 경우
            if(interestRepository.existsByName(interestName)){
                // 관심사 검색
                Interest interest = interestRepository.findByName(interestName);
                // 관계 중복 형성을 막기 위함
                if(!userInterestRepository.existsByUserIdAndInterestId(createUserProfileReq.getUserId(), interest.getId())) {
                    // 관계 주입
                    UserInterest userInterest = UserInterest.builder()
                            .user(user)
                            .interest(interest)
                            .build();
                    userInterestRepository.save(userInterest);
                }
            }
            else{ // 기존에 존재하지 않는 관심사일 경우
                // 관심사 생성
                Interest interest = Interest.builder()
                        .name(interestName)
                        .build();
                interestRepository.save(interest);
                // 관계 주입
                UserInterest userInterest = UserInterest.builder()
                        .user(user)
                        .interest(interest)
                        .build();
                userInterestRepository.save(userInterest);
            }
        }

        // res : 관심사
        List<UserInterest> userInterests = userInterestRepository.findAllByUserId(createUserProfileReq.getUserId());
        if(!userInterests.isEmpty()){
            for(UserInterest ui : userInterests){
                userProfileRes.addInterest(ui.getInterest().getName());
            }
        }

        // res : 프로필 사진
        userProfileRes.setProfileImg(awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        List<UserImage> userImageList = userImageRepository.findAllByUserId(user.getId());
        for (UserImage userImage : userImageList){
            if(userImage.getStatus().equals(BaseStatus.INACTIVE)) continue;
            userProfileRes.addImage(awsS3Service.makeUrlOfFilename(userImage.getImgUUid()));
        }

        return ApplicationResponse.ok(userProfileRes);
    }

    @Override
    public ApplicationResponse<UserProfileRes> getProfile(GetUserProfileReq getUserProfileReq){

        tokenService.validateRefreshToken(getUserProfileReq.getRefreshTokenUserId(), getUserProfileReq.getRefreshToken());

        User user = userRepository.findByUserId(getUserProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        // 차단 유무
        if(blockService.isBlockingUser(getUserProfileReq.getRefreshTokenUserId(), getUserProfileReq.getUserId())) userProfileRes.setIsBlockingUser(1);
        else userProfileRes.setIsBlockingUser(0);

        // 언어
        List<Language> languages = languageRepository.findAllByUserId(getUserProfileReq.getUserId());
        if(!languages.isEmpty()){
            for(Language l : languages){
                userProfileRes.addLanguage(l.getCode(), l.getLevel());
            }
        }

        // 관심사
        List<UserInterest> userInterests = userInterestRepository.findAllByUserId(getUserProfileReq.getUserId());
        if(!userInterests.isEmpty()){
            for(UserInterest ui : userInterests){
                userProfileRes.addInterest(ui.getInterest().getName());
            }
        }

        // 프로필 사진
        userProfileRes.setProfileImg(awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        List<UserImage> userImageList = userImageRepository.findAllByUserId(user.getId());
        for (UserImage userImage : userImageList){
            if(userImage.getStatus().equals(BaseStatus.INACTIVE)) continue;
            userProfileRes.addImage(awsS3Service.makeUrlOfFilename(userImage.getImgUUid()));
        }

        return ApplicationResponse.ok(userProfileRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<Void> delProfile(Long userId){
        User user = userRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        // 유저 탈퇴시 이름, 대표 사진 null 처리
        user.delUser();

        // image 엔티티 삭제
        userImageRepository.deleteAllByUserId(user.getId());

        return ApplicationResponse.ok();
    }

    @Override
    public ApplicationResponse<UserImagesRes> getUserImage(GetUserImageReq getUserImageReq){

        tokenService.validateRefreshToken(getUserImageReq.getUserId(), getUserImageReq.getRefreshToken());

        User user = userRepository.findByUserId(getUserImageReq.getUserId()).orElseThrow(NotFoundUserException::new);
        UserImagesRes userImagesRes = new UserImagesRes();

        if(user.getUserImages() != null) userImagesRes.setProfileImageUrl(user.getProfileImg());

        List<UserImage> userImages = userImageRepository.findAllByUserId(getUserImageReq.getUserId());
        if (!userImages.isEmpty()){
            for(UserImage i : userImages){
                if(i.getStatus().equals(BaseStatus.INACTIVE)) continue;
                userImagesRes.addImage(awsS3Service.makeUrlOfFilename(i.getImgUUid()), i.getId());
            }
        }

        return ApplicationResponse.ok(userImagesRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<UserImagesRes> AddAndDeleteUserImage(AddAndDeleteUserImageReq addAndDeleteUserImageReq){

        tokenService.validateRefreshToken(addAndDeleteUserImageReq.getUserId(), addAndDeleteUserImageReq.getRefreshToken());

        User user = userRepository.findByUserId(addAndDeleteUserImageReq.getUserId()).orElseThrow(NotFoundUserException::new);

        // 프로필 이미지 삭제
        if(addAndDeleteUserImageReq.getDeleteUserImageIds() != null) deleteUserImage(addAndDeleteUserImageReq.getDeleteUserImageIds());

        // 대표 프로필 이미지 업로드
        if(addAndDeleteUserImageReq.getUploadProfileImage() != null) enterUserProfileImage(user, addAndDeleteUserImageReq.getUploadProfileImage());

        // 이외 프로필 이미지 업로드
        if(addAndDeleteUserImageReq.getUploadImages() != null) enterUserImages(user, addAndDeleteUserImageReq.getUploadImages());

        UserImagesRes userImagesRes = new UserImagesRes();
        userImagesRes.setProfileImageUrl(awsS3Service.makeUrlOfFilename(user.getProfileImg()));
        List<UserImage> userImageList = userImageRepository.findAllByUserId(user.getId());
        for (UserImage userImage : userImageList){
            if(userImage.getStatus().equals(BaseStatus.INACTIVE)) continue;
            userImagesRes.addImage(awsS3Service.makeUrlOfFilename(userImage.getImgUUid()), userImage.getId());
        }

        return ApplicationResponse.ok(userImagesRes);
    }

    public Void enterUserProfileImage(User user, MultipartFile uploadProfileImage){

        String mainImageUUid = awsS3Service.uploadImage(uploadProfileImage);
        user.changeMainImgUUid(mainImageUUid);

        return null;
    }

    public Void enterUserImages(User user, List<MultipartFile> uploadImages){

        if(uploadImages != null){
            List<String> imageUUids = awsS3Service.uploadImages(uploadImages);
            for(String i : imageUUids){
                UserImage userImage = UserImage.builder()
                        .user(user)
                        .imgUUid(i)
                        .build();
                userImageRepository.save(userImage);
            }
        }

        return null;
    }

    public Void deleteUserImage(List<Long> deleteUserImageIds){

        for (Long deleteUserImageId : deleteUserImageIds){
            UserImage userImage = userImageRepository.findById(deleteUserImageId).orElseThrow(() ->new NotFoundUserImageException());
            userImage.deleteUserImage(); // BASE_STATUS : INACTIVE로 변경
        }

        return null;
    }

    @Override
    @Transactional
    public ApplicationResponse<Void> createUser(CreateUserReq createUserReq){

        if(userRepository.existsByLoginId(createUserReq.getLoginId())) throw new UserDuplicationLoginId();

        userRepository.save(CreateUserReq.toEntityUser(createUserReq));

        return ApplicationResponse.ok();
    }

    @Override
    @Transactional
    public User createUserApple(CreateUserAppleReq createUserAppleReq){

        if(userRepository.existsByLoginId(createUserAppleReq.getLoginId())) throw new UserDuplicationLoginId();

        User user = userRepository.save(CreateUserAppleReq.toEntityUserApple(createUserAppleReq));

        return user;
    }

    @Override
    @Transactional
    public ApplicationResponse<UserDeviceTokenRes> addDeviceToken(AddUserDeviceTokenReq addUserDeviceTokenReq){

        tokenService.validateRefreshToken(addUserDeviceTokenReq.getUserId(), addUserDeviceTokenReq.getRefreshToken());

        User user = userRepository.findByUserId(addUserDeviceTokenReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.addDeviceToken(addUserDeviceTokenReq.getDeviceToken());

        UserDeviceTokenRes userDeviceTokenRes = new UserDeviceTokenRes(user.getId(), user.getDeviceToken());
        
        return ApplicationResponse.ok(userDeviceTokenRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<LogoutRes> logout(LogoutReq logoutReq){

        tokenService.validateRefreshToken(logoutReq.getUserId(), logoutReq.getRefreshToken());

        User user = userRepository.findByUserId(logoutReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.changeUserStatus(UserStatus.LOGOUT);

        LogoutRes logoutRes = new LogoutRes(UserStatus.LOGOUT.toString());

        return ApplicationResponse.ok(logoutRes);
    }

}
