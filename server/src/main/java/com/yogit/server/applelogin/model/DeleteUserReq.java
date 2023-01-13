package com.yogit.server.applelogin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteUserReq {
    Long userId;
    String refreshToken;

    String identityToken;
}
