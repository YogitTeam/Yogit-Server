package com.yogit.server.user.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.AddUserAdditionalProfileReq;
import com.yogit.server.user.dto.request.CreateUserEssentialProfileReq;
import com.yogit.server.user.dto.request.CreateUserImageReq;
import com.yogit.server.user.dto.request.CreateUserReq;
import com.yogit.server.user.dto.response.UserAdditionalProfileRes;
import com.yogit.server.user.dto.response.UserEssentialProfileRes;
import com.yogit.server.user.dto.response.UserImagesRes;
import com.yogit.server.user.dto.response.UserProfileRes;
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
    @ApiOperation(value = "유저 필수 정보 입력 및 수정", notes = "sms 인증을 한 경우에는 phoneNum을 꼭 입력해주세요.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "userName", dataTypeClass = String.class, example = "강신현"),
            @ApiImplicitParam(name = "userAge", dataTypeClass = int.class, example = "25"),
            @ApiImplicitParam(name = "gender", dataTypeClass = String.class, example = "Male"),
            @ApiImplicitParam(name = "nationality", dataTypeClass = String.class, example = "Korea")
    })
    @PatchMapping("/essential-profile")
    public ApplicationResponse<UserEssentialProfileRes> enterEssentialProfile(@ModelAttribute CreateUserEssentialProfileReq createUserEssentialProfileReq){
        return userService.enterEssentialProfile(createUserEssentialProfileReq);
    }

    /**
     * 유저 Profile 조회
     * @author 강신현
     */
    @ApiOperation(value = "유저 Profile 조회")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @GetMapping("/prifile/{userId}") // TODO
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
    @ApiOperation(value = "유저 사진 등록", notes = "swagger 에서 이미지(multipartfile)처리가 잘 되지 않으므로, postman으로 테스트 바랍니다. https://solar-desert-882435.postman.co/workspace/3e0fe8f2-15e0-41c4-9fcd-b614a975c12a/request/18177198-32a7b164-ac0b-417d-951d-46b205ac62aa")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @PostMapping("/image")
    public ApplicationResponse<UserImagesRes> enterUserImage(@ModelAttribute CreateUserImageReq createUserImageReq){
        return userService.enterUserImage(createUserImageReq);
    }

    /**
     * 유저 사진 조회
     * @author 강신현
     */
    @ApiOperation(value = "유저 사진 조회")
    @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "0")
    @GetMapping("/image/{userId}")
    public ApplicationResponse<UserImagesRes> getUserImage(@PathVariable Long userId){
        return userService.getUserImage(userId);
    }

    /**
     * 유저 추가 정보 입력
     * @author 강신현
     */
    @ApiOperation(value = "유저 추가 정보 입력")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "latitude", dataTypeClass = Float.class, example = "10.23"),
            @ApiImplicitParam(name = "longitude", dataTypeClass = Float.class, example = "10.23"),
            @ApiImplicitParam(name = "aboutMe", dataTypeClass = String.class, example = "캠핑 모임에 관심이 많습니다."),
            @ApiImplicitParam(name = "cityId", dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "job", dataTypeClass = String.class, example = "학생"),
            @ApiImplicitParam(name = "administrativeArea", dataTypeClass = String.class, example = "행신동")
    })
    @PatchMapping("/additional-profile")
    public ApplicationResponse<UserAdditionalProfileRes> enterAdditionalProfile(@ModelAttribute AddUserAdditionalProfileReq addUserAdditionalProfileReq){
        return userService.enterAdditionalProfile(addUserAdditionalProfileReq);
    }

    /**
     * 유저 회원가입 (일반)
     */
    @ApiOperation(value = "유저 회원가입", notes = "sms 인증이 완료되어야 회원가입이 가능합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", required = true, dataTypeClass = String.class, example = "kang123"),
            @ApiImplicitParam(name = "phoneNum", required = true, dataTypeClass = String.class, example = "01012345678")
    })
    @PostMapping("/join")
    public ApplicationResponse<Void> createUser(@ModelAttribute CreateUserReq createUserReq){
        return userService.createUser(createUserReq);
    }


    /**
     * 유저 로그인
     */

}
