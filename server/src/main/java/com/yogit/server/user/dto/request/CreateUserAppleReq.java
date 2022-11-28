package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.User;
import com.yogit.server.user.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserAppleReq {

    String loginId;
    String refreshToken;
    String name;
    UserType userType;

    public static User toEntityUserApple(CreateUserAppleReq createUserAppleReq){
        User user = new User(createUserAppleReq.loginId, createUserAppleReq.refreshToken, createUserAppleReq.name, createUserAppleReq.userType);
        return user;
    }
}
