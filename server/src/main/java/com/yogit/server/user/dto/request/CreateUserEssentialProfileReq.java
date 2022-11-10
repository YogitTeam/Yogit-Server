package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.Nationality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserEssentialProfileReq {

    Long userId;

    String userName;
    int userAge;
    String gender;
    Nationality nationality;

    List<String> languageNames;
    List<String> languageLevels;
}
