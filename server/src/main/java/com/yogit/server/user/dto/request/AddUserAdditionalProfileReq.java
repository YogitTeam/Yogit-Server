package com.yogit.server.user.dto.request;

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
    String job;
    String administrativeArea;

    Long cityId;
    List<String> interests;
}
