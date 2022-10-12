package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditUserEssentialProfileReq {

    Long userId;

    String userName;
    Integer userAge;
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
}
