package com.yogit.server.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddUserDeviceTokenReq {
    Long userId;
    String refreshToken;
    String deviceToken;
}
