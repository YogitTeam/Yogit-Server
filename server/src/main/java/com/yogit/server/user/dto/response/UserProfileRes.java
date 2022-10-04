package com.yogit.server.user.dto.response;

import com.yogit.server.user.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileRes {
    String about_me;
    String about_me_interest;
    String about_me_job;

    int age;
    String name;
    Gender gender; // Prefer not to say, male, female 중 하나
    float member_temp;
    Nationality nationality;
    String profile_img; // 대표 프로필 이미지

//    String login_id; //TODO
//    String pass_word; //TODO

    LanguageName languageName1;
    LanguageLevel languageLevel1;

    LanguageName languageName2;
    LanguageLevel languageLevel2;

    LanguageName languageName3;
    LanguageLevel languageLevel3;

    LanguageName languageName4;
    LanguageLevel languageLevel4;

    LanguageName languageName5;
    LanguageLevel languageLevel5;

    public static UserProfileRes create(User user){
        UserProfileRes userProfileRes = new UserProfileRes();

        userProfileRes.about_me = user.getAboutMe();
        userProfileRes.about_me_interest = user.getAboutMeInterest();
        userProfileRes.about_me_job = user.getAboutMeJob();

        userProfileRes.age = user.getAge();
        userProfileRes.name = user.getName();
        userProfileRes.gender = user.getGender();
        userProfileRes.member_temp = user.getMemberTemp();
        userProfileRes.nationality = user.getNationality();
        userProfileRes.profile_img = user.getProfileImgUrl();

        return userProfileRes;
    }

    public void addLanguage(Language language){
        if(this.languageName1 == null){
            this.languageName1 = language.getName();
            this.languageLevel1 = language.getLevel();
        }
        else if(this.languageName2 == null){
            this.languageName2 = language.getName();
            this.languageLevel2 = language.getLevel();
        }
        else if(this.languageName3 == null){
            this.languageName3 = language.getName();
            this.languageLevel3 = language.getLevel();
        }
        else if(this.languageName4 == null){
            this.languageName4 = language.getName();
            this.languageLevel4 = language.getLevel();
        }
        else{
            this.languageName5 = language.getName();
            this.languageLevel5 = language.getLevel();
        }
    }
}
