package com.yogit.server.user.dto.response;

import com.yogit.server.user.entity.Gender;
import com.yogit.server.user.entity.Nationality;
import com.yogit.server.user.entity.User;
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

    public static UserProfileRes create(User user){
        UserProfileRes userProfileRes = new UserProfileRes();

        userProfileRes.about_me = user.getAboutMe();

        userProfileRes.age = user.getAge();
        userProfileRes.name = user.getName();
        userProfileRes.gender = user.getGender();
        userProfileRes.member_temp = user.getMemberTemp();
        userProfileRes.nationality = user.getNationality();
        userProfileRes.profile_img = user.getProfileImg();

        return userProfileRes;
    }
}
