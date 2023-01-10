package com.yogit.server.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserEssentialProfileReq {

    Long userId;

    String userName;
    int userAge;
    String gender;
    String nationality;

    List<String> languageNames;
    List<String> languageLevels;

    MultipartFile uploadProfileImage; // 대표 프로필 이미지
    List<MultipartFile> uploadImages; // 이외 프로필 이미지들
    List<Long> deleteUserImageIds;

    String refreshToken;
}
