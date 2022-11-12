package com.yogit.server.user.dto.response;

import com.yogit.server.user.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileRes {

    Long userId;

    // 유저 필수 정보
    Integer age;
    String name;
    String gender;
    float memberTemp;
    String nationality;
    String profileImg; // 대표 프로필 이미지
    String aboutMe;
    String login_id;
    UserStatus userStatus;
    String phone;

    List<String> languageNames = new ArrayList<>();
    List<String> languageLevels = new ArrayList<>();

    // 유저 추가 정보
    String administrativeArea;
    float longtitude;
    float latitude;

    CityName city;

    List<String> interests = new ArrayList<>();

    public static UserProfileRes create(User user){
        UserProfileRes userProfileRes = new UserProfileRes();

        userProfileRes.userId = user.getId();

        // 유저 필수 정보
        userProfileRes.age = user.getAge();
        userProfileRes.name = user.getName();
        userProfileRes.gender = user.getGender();
        userProfileRes.nationality = user.getNationality();
        userProfileRes.profileImg = user.getProfileImg();
        userProfileRes.aboutMe = user.getAboutMe();
        userProfileRes.login_id = user.getLoginId();
        userProfileRes.userStatus = user.getUserStatus();
        userProfileRes.phone = user.getPhoneNum();

        // 유저 추가 정보
        userProfileRes.administrativeArea = user.getAdministrativeArea();
        userProfileRes.longtitude = user.getLongtitude();
        userProfileRes.latitude = user.getLatitude();

        return userProfileRes;
    }

    public void addCity(CityName city){
        this.city = city;
    }

    public void addLanguage(String languageName, String languageLevel){
        this.languageNames.add(languageName);
        this.languageLevels.add(languageLevel);
    }

    public void addInterest(String interest){
        this.interests.add(interest);
    }
}
