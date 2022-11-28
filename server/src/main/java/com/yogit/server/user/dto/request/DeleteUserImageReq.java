package com.yogit.server.user.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteUserImageReq {
    Long userId;
    Long userImageId;
}
