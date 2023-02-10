package com.yogit.server.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserProfileReq {
    Long userId;

    String userName;
    int userAge;
    String gender;
    String nationality;

    List<String> languageCodes = new ArrayList<>();
    List<Integer> languageLevels = new ArrayList<>();

    float latitude;
    float longitude;
    String aboutMe;
    String job;

    String cityName;
    List<String> interests = new ArrayList<>();

    String refreshToken;
}
