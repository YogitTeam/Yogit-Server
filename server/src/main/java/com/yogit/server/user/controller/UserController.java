package com.yogit.server.user.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.AddUserAdditionalProfileReq;
import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.dto.request.CreateUserImageReq;
import com.yogit.server.user.dto.request.EditUserEssentialProfileReq;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;
import com.yogit.server.user.entity.*;
import com.yogit.server.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    /**
     * 유저 필수 정보 입력
     * @author 강신현
     */
    @ApiOperation(value = "유저 필수 정보 입력", notes = "languageName1 부터, languageLevel1 부터 차례로 입력해주세요 (swagger에서 enum 열거형을 지원하지 않으므로)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "userAge", required = true, dataTypeClass = int.class, example = "0"),
            @ApiImplicitParam(name = "gender", required = true, dataTypeClass = Gender.class),
            @ApiImplicitParam(name = "nationality", required = true, dataTypeClass = Nationality.class),
            @ApiImplicitParam(name = "languageName1", required = true, dataTypeClass = LanguageName.class),
            @ApiImplicitParam(name = "languageLevel1", required = true, dataTypeClass = LanguageLevel.class)
    })
    @PostMapping("/essential-profile")
    public ApplicationResponse<UserProfileRes> enterEssentialProfile(@ModelAttribute CreateUserEssentialProfileReq createUserEssentialProfileReq){
        return userService.enterEssentialProfile(createUserEssentialProfileReq);
    }

    /**
     * 유저 필수 정보 수정
     * @author 강신현
     */
    @ApiOperation(value = "유저 필수 정보 수정", notes = "수정할 정보들만 입력해주세요, Language 중에 하나라도 변경사항이 있다면 유저의 모든 Language 를 입력하여 요청해주세요")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @PatchMapping("/essential-profile")
    public ApplicationResponse<UserProfileRes> editEssentialProfile(@ModelAttribute EditUserEssentialProfileReq editUserEssentialProfileReq){
        return userService.editEssentialProfile(editUserEssentialProfileReq);
    }

    /**
     * 유저 Profile 조회
     * @author 강신현
     */
    @ApiOperation(value = "유저 Profile 조회")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @GetMapping("/prifile/{userId}")
    public ApplicationResponse<UserProfileRes> getProfile(@PathVariable Long userId){
        return userService.getProfile(userId);
    }

    /**
     * 유저 Profile 삭제
     * @author 강신현
     */
    @ApiOperation(value = "유저 Profile 삭제")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @PatchMapping("/{userId}")
    public ApplicationResponse<Void> delProfile(@PathVariable Long userId){
        return userService.delProfile(userId);
    }

    /**
     * 유저 Profile + 사진 등록
     * @author 강신현
     */
    @ApiOperation(value = "유저 Profile 사진 등록", notes = "swagger 에서 이미지(multipartfile)처리가 잘 되지 않으므로, postman으로 테스트 바랍니다. https://solar-desert-882435.postman.co/workspace/3e0fe8f2-15e0-41c4-9fcd-b614a975c12a/request/18177198-32a7b164-ac0b-417d-951d-46b205ac62aa")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @PostMapping("/image")
    public ApplicationResponse<UserImagesRes> enterUserImage(@ModelAttribute CreateUserImageReq createUserImageReq){
        return userService.enterUserImage(createUserImageReq);
    }

    /**
     * 유저 추가 정보 입력
     * @author 강신현
     */
    @ApiOperation(value = "유저 추가 정보 입력")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @PatchMapping("/additional-profile")
    public ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(@ModelAttribute AddUserAdditionalProfileReq addUserAdditionalProfileReq){
        return userService.enterAdditionalProfile(addUserAdditionalProfileReq);
    }
}
