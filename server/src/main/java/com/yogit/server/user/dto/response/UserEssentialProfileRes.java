package com.yogit.server.user.dto.response;

import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.entity.Gender;
import com.yogit.server.user.entity.Nationality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEssentialProfileRes {

    Long userId;

    String userName;
    Integer userAge;
    Gender gender;
    Nationality nationality;

    List<String> languageNames = new ArrayList<>();
    List<String> languageLevels = new ArrayList<>();

    public static UserEssentialProfileRes create(CreateUserEssentialProfileReq createUserEssentialProfileReq){
        UserEssentialProfileRes userEssentialProfileRes = new UserEssentialProfileRes();

        userEssentialProfileRes.userId = createUserEssentialProfileReq.getUserId();
        userEssentialProfileRes.userName = createUserEssentialProfileReq.getUserName();
        userEssentialProfileRes.userAge = createUserEssentialProfileReq.getUserAge();
        userEssentialProfileRes.gender = createUserEssentialProfileReq.getGender();
        userEssentialProfileRes.nationality = createUserEssentialProfileReq.getNationality();

        return userEssentialProfileRes;
    }

    public void addLanguage(String languageName, String languageLevel){
        this.languageNames.add(languageName);
        this.languageLevels.add(languageLevel);
    }
}
