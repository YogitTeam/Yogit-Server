package com.yogit.server.applelogin.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@NoArgsConstructor
public class ServicesResponse {

    private String state;
    private String code; // authorization_token
    private String id_token; // identity_token
    private JSONObject user; // 애플에서 제공하는 유저 정보
    private String identifier;
    private Boolean hasRequirementInfo; // 유저 필수정보 입력 여부
}
