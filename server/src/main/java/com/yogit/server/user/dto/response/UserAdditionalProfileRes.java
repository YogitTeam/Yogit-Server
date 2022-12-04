package com.yogit.server.user.dto.response;

import com.yogit.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAdditionalProfileRes {
    Long userId;

    float latitude;
    float longitude;
    String aboutMe;

    String cityName;
    List<String> interests = new ArrayList<>();

    public static UserAdditionalProfileRes create(User user){
        UserAdditionalProfileRes userAdditionalProfileRes = new UserAdditionalProfileRes();

        userAdditionalProfileRes.userId = user.getId();
        userAdditionalProfileRes.latitude = user.getLatitude();
        userAdditionalProfileRes.longitude = user.getLongtitude();
        userAdditionalProfileRes.aboutMe = user.getAboutMe();

        return userAdditionalProfileRes;
    }

}
