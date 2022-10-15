package com.yogit.server.user.dto.request;

import com.yogit.server.user.entity.User;
import com.yogit.server.user.entity.UserImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserImageReq {

    Long userId;
    MultipartFile profileImage; // 대표 프로필 사진
    List<MultipartFile> images; // 최대 5개

    public static UserImage toEntityUserImage(User user, String imgUUid){
        return UserImage.builder()
                .user(user)
                .imgUUid(imgUUid)
                .build();
    }
}

