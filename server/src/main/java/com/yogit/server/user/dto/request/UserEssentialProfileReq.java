package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEssentialProfileReq {

    // List<MultipartFile> userImages; // 최대 6개 -> TODO user_image 엔티티 따로 만들어서 구현
    String userName;
    int userAge;
    Gender gender; // Prefer not to say, male, female 중 하나
    Nationality nationality;

//    LanguageName languageName1;
//    LanguageLevel languageLevel1;
//    LanguageName languageName2;
//    LanguageLevel languageLevel2;
//    LanguageName languageName3;
//    LanguageLevel languageLevel3;
//    LanguageName languageName4;
//    LanguageLevel languageLevel4;
//    LanguageName languageName5;
//    LanguageLevel languageLevel5;

    public static User toEntity(UserEssentialProfileReq userEssentialProfileReq){
        return User.builder()
                .name(userEssentialProfileReq.getUserName())
                .age(userEssentialProfileReq.getUserAge())
                .gender(userEssentialProfileReq.getGender())
                .nationality(userEssentialProfileReq.getNationality())
                .build();
    }
}
