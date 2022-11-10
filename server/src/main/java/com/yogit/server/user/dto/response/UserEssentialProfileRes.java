package com.yogit.server.user.dto.response;

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
    String gender;
    Nationality nationality;

    List<String> languageNames = new ArrayList<>();
    List<String> languageLevels = new ArrayList<>();

    public static UserEssentialProfileRes create(Long userId, String userName, Integer userAge, String gender, Nationality nationality){
        UserEssentialProfileRes userEssentialProfileRes = new UserEssentialProfileRes();

        userEssentialProfileRes.userId = userId;
        userEssentialProfileRes.userName = userName;
        userEssentialProfileRes.userAge = userAge;
        userEssentialProfileRes.gender = gender;
        userEssentialProfileRes.nationality = nationality;

        return userEssentialProfileRes;
    }

    public void addLanguage(String languageName, String languageLevel){
        this.languageNames.add(languageName);
        this.languageLevels.add(languageLevel);
    }
}
