package com.yogit.server.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditUserEssentialProfileReq {

    Long userId;

    String userName;
    Integer userAge;
    String gender;
    String nationality;

    List<String> languageNames;
    List<String> languageLevels;
}
