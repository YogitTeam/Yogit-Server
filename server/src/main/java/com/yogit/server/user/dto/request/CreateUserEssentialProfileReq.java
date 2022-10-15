package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserEssentialProfileReq {

    String userName;
    int userAge;
    Gender gender;
    Nationality nationality;

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

    public static User toEntityUser(CreateUserEssentialProfileReq createUserEssentialProfileReq){
        return User.builder()
                .name(createUserEssentialProfileReq.getUserName())
                .age(createUserEssentialProfileReq.getUserAge())
                .gender(createUserEssentialProfileReq.getGender())
                .nationality(createUserEssentialProfileReq.getNationality())
                .build();
    }
}
