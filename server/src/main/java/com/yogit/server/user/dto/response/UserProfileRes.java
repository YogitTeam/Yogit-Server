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
    List<String> imageUrls = new ArrayList<>();

    String aboutMe;
    UserStatus userStatus;
    String phone;
    String job;

    List<String> languageCodes = new ArrayList<>();
    List<Integer> languageLevels = new ArrayList<>();

    // 유저 추가 정보
    float longtitude;
    float latitude;

    String locality;

    List<String> interests = new ArrayList<>();

    public static UserProfileRes create(User user){
        UserProfileRes userProfileRes = new UserProfileRes();

        userProfileRes.userId = user.getId();

        // 유저 필수 정보
        userProfileRes.age = user.getAge();
        userProfileRes.name = user.getName();
        userProfileRes.gender = user.getGender();
        userProfileRes.nationality = user.getNationality();
        userProfileRes.aboutMe = user.getAboutMe();
        userProfileRes.userStatus = user.getUserStatus();
        userProfileRes.phone = user.getPhoneNum();
        userProfileRes.job = user.getJob();

        // 유저 추가 정보
        if(user.getLongtitude() != null)userProfileRes.longtitude = user.getLongtitude();
        if(user.getLatitude() != null)userProfileRes.latitude = user.getLatitude();

        return userProfileRes;
    }

    public void addLanguage(String languageCode, Integer languageLevel){
        this.languageCodes.add(languageCode);
        this.languageLevels.add(languageLevel);
    }

    public void addInterest(String interest){
        this.interests.add(interest);
    }

    public void addImage(String url){
        this.imageUrls.add(url);
    }
}
