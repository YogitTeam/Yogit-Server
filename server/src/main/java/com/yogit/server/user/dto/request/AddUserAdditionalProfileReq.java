package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.CityName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddUserAdditionalProfileReq {

    Long userId;

    float latitude;
    float longitude;
    String aboutMe;

    CityName city;
    List<String> interests;
}
