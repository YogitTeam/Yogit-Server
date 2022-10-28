package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserReq {

    String loginId;

    public static User toEntityUser(CreateUserReq createUserReq){
        return User.builder()
                .loginId(createUserReq.getLoginId())
                .build();
    }
}
