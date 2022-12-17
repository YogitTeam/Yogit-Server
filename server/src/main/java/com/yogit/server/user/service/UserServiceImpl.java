package com.yogit.server.user.service;

import com.yogit.server.applelogin.exception.InvalidRefreshTokenException;
import com.yogit.server.applelogin.exception.NotFoundRefreshTokenException;
import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.s3.AwsS3Service;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.*;
import com.yogit.server.user.entity.*;
import com.yogit.server.user.exception.*;
import com.yogit.server.user.repository.*;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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

    /**
     * 리프레시 토큰 검증
     *
     * refresh_token은 만료되지 않기 때문에 권한이 필요한 요청일 경우
     * 굳이 매번 애플 ID 서버로부터 refresh_token을 통해 access_token을 발급 받기보다는
     * 유저의 refresh_token을 따로 DB나 기타 저장소에 저장해두고 캐싱해두고 조회해서 검증하는편이 성능면에서 낫다는 자료를 참고
     * https://hwannny.tistory.com/71
     */
    @Override
    @Transactional(readOnly = true)
    public Void validateRefreshToken(Long userId, String refreshToken){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundUserException());

        if(user.getRefreshToken() == null) throw new NotFoundRefreshTokenException();

        if(!user.getRefreshToken().equals(refreshToken)) throw new InvalidRefreshTokenException();

        return null;
    }

    @Transactional
    @Override
    public ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(CreateUserEssentialProfileReq createUserEssentialProfileReq){

        validateRefreshToken(createUserEssentialProfileReq.getUserId(), createUserEssentialProfileReq.getRefreshToken());

        if(!createUserEssentialProfileReq.getGender().equals("Prefer not to say") && !createUserEssentialProfileReq.getGender().equals("Male") && !createUserEssentialProfileReq.getGender().equals("Female")) throw new UserGenderException();

        User user = userRepository.findByUserId(createUserEssentialProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);
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
    public ApplicationResponse<UserProfileRes> getProfile(GetUserProfileReq getUserProfileReq){

        validateRefreshToken(getUserProfileReq.getRefreshTokenUserId(), getUserProfileReq.getRefreshToken());

        User user = userRepository.findByUserId(getUserProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);

        UserProfileRes userProfileRes = UserProfileRes.create(user);

        List<Language> languages = languageRepository.findAllByUserId(getUserProfileReq.getUserId());
        if(!languages.isEmpty()){
            for(Language l : languages){
                userProfileRes.addLanguage(l.getName(), l.getLevel());
            }
        }

        List<UserInterest> userInterests = userInterestRepository.findAllByUserId(getUserProfileReq.getUserId());
        if(!userInterests.isEmpty()){
            for(UserInterest ui : userInterests){
                userProfileRes.addInterest(ui.getInterest().getName());
            }
        }

        userProfileRes.setProfileImg(awsS3Service.makeUrlOfFilename(user.getProfileImg()));

        try {

            URL url = new URL("http://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?ServiceKey=Os%2B%2Fa%2BWGJPptb5Rf1U850JQo11XO0fCA5cL3YND%2BxoxUm8B38IDZjHKlrpV0gj496%2Br53Rg61EdzI9KDuILDrg%3D%3D" + "&cond[country_iso_alp2::EQ]=" + user.getNationality());

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray data = (JSONArray) jsonObject.get("data");


            JSONObject nation = (JSONObject) data.get(0);

            String country_eng_nm = nation.get("country_eng_nm").toString();
            String download_url = nation.get("download_url").toString();

            userProfileRes.setCountry_eng_nm(country_eng_nm);
            userProfileRes.setDownload_url(download_url);

        } catch (Exception e) {
            e.printStackTrace();
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
    @Transactional
    public ApplicationResponse<UserImagesRes> enterUserImage(CreateUserImageReq createUserImageReq){

        validateRefreshToken(createUserImageReq.getUserId(), createUserImageReq.getRefreshToken());

        User user = userRepository.findByUserId(createUserImageReq.getUserId()).orElseThrow(NotFoundUserException::new);
        UserImagesRes userImagesRes = new UserImagesRes();

        if(createUserImageReq.getProfileImage().isEmpty()) throw new NotFoundUserProfileImg();

        // 메인 프로필 사진 업로드
        String mainImageUUid = awsS3Service.uploadImage(createUserImageReq.getProfileImage());
        user.changeMainImgUUid(mainImageUUid);
        userImagesRes.setProfileImageUrl(awsS3Service.makeUrlOfFilename(mainImageUUid));

        // 나머지 사진 업로드
        if(createUserImageReq.getImages() != null){
            List<String> imageUUids = awsS3Service.uploadImages(createUserImageReq.getImages());
            for(String i : imageUUids){
                UserImage userImage = createUserImageReq.toEntityUserImage(user, i);
                userImageRepository.save(userImage);
                userImagesRes.addImage(awsS3Service.makeUrlOfFilename(userImage.getImgUUid()));
            }
        }

        return ApplicationResponse.ok(userImagesRes);
    }

    @Override
    public ApplicationResponse<UserImagesRes> getUserImage(GetUserImageReq getUserImageReq){

        validateRefreshToken(getUserImageReq.getUserId(), getUserImageReq.getRefreshToken());

        User user = userRepository.findByUserId(getUserImageReq.getUserId()).orElseThrow(NotFoundUserException::new);
        UserImagesRes userImagesRes = new UserImagesRes();

        if(user.getUserImages() != null) userImagesRes.setProfileImageUrl(user.getProfileImg());

        List<UserImage> userImages = userImageRepository.findAllByUserId(getUserImageReq.getUserId());
        if (!userImages.isEmpty()){
            for(UserImage i : userImages){
                userImagesRes.addImage(awsS3Service.makeUrlOfFilename(i.getImgUUid()), i.getId());
            }
        }

        return ApplicationResponse.ok(userImagesRes);
    }


    @Override
    @Transactional
    public ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(AddUserAdditionalProfileReq addUserAdditionalProfileReq){

        validateRefreshToken(addUserAdditionalProfileReq.getUserId(), addUserAdditionalProfileReq.getRefreshToken());

        User user = userRepository.findByUserId(addUserAdditionalProfileReq.getUserId()).orElseThrow(NotFoundUserException::new);

        user.addAdditionalProfile(addUserAdditionalProfileReq.getLatitude(), addUserAdditionalProfileReq.getLongitude(), addUserAdditionalProfileReq.getAboutMe(), addUserAdditionalProfileReq.getJob());

        UserAdditionalProfileRes userAdditionalProfileRes = UserAdditionalProfileRes.create(user);

        // 기존에 존재하는 city인 경우
        if(cityRepository.existsByCityName(addUserAdditionalProfileReq.getCityName())){
            City city = cityRepository.findByCityName(addUserAdditionalProfileReq.getCityName());
            city.addUser(user);
        }
        else{ // 기존에 존재하지 않는 city인 경우
            City city = City.builder()
                    .user(user)
                    .cityName(addUserAdditionalProfileReq.getCityName())
                    .build();
            cityRepository.save(city);
            city.addUser(user);
        }

        userAdditionalProfileRes.setCityName(addUserAdditionalProfileReq.getCityName());

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

    @Override
    @Transactional
    public User createUserApple(CreateUserAppleReq createUserAppleReq){

        if(userRepository.existsByLoginId(createUserAppleReq.getLoginId())) throw new UserDuplicationLoginId();

        User user = userRepository.save(CreateUserAppleReq.toEntityUserApple(createUserAppleReq));

        return user;
    }

    @Override
    @Transactional
    public ApplicationResponse<UserImagesRes> deleteUserImage(DeleteUserImageReq deleteUserImageReq){

        validateRefreshToken(deleteUserImageReq.getUserId(), deleteUserImageReq.getRefreshToken());

        User user = userRepository.findByUserId(deleteUserImageReq.getUserId())
                .orElseThrow(() -> new NotFoundUserException());

        UserImage userImage = userImageRepository.findById(deleteUserImageReq.getUserImageId()).orElseThrow(() ->new NotFoundUserImageException());
        //userImage.deleteUserImage(); // 삭제 -> BASE_STATUS : INACTIVE로 변경
        userImageRepository.delete(userImage);

        UserImagesRes userImagesRes = new UserImagesRes();
        userImagesRes.setProfileImageUrl(awsS3Service.makeUrlOfFilename(user.getProfileImg()));

        List<UserImage> userImages = userImageRepository.findAllByUserId(user.getId());
        if (!userImages.isEmpty()){
            for(UserImage i : userImages){
                if(i.equals(userImage))continue;
                userImagesRes.addImage(awsS3Service.makeUrlOfFilename(i.getImgUUid()), i.getId());
            }
        }

        return ApplicationResponse.ok(userImagesRes);
    }

    @Override
    @Transactional
    public ApplicationResponse<UserDeviceTokenRes> addDeviceToken(AddUserDeviceTokenReq addUserDeviceTokenReq){

        validateRefreshToken(addUserDeviceTokenReq.getUserId(), addUserDeviceTokenReq.getRefreshToken());

        User user = userRepository.findByUserId(addUserDeviceTokenReq.getUserId()).orElseThrow(NotFoundUserException::new);
        user.addDeviceToken(addUserDeviceTokenReq.getDeviceToken());

        UserDeviceTokenRes userDeviceTokenRes = new UserDeviceTokenRes(user.getId(), user.getDeviceToken());
        
        return ApplicationResponse.ok(userDeviceTokenRes);
    }
}
