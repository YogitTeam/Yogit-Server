package com.yogit.server.applelogin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenResponse {

    private String access_token;
    private Long expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;

    // 유저 엔티티 생성 후 res
    private String name;
    private String email;

    //추가적인 응답 필드
    private String userType; // ex)apple
    private Account account;
}
