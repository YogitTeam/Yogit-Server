package com.yogit.server.user.controller;

import com.yogit.server.global.dto.ApplicationResponse;
import com.yogit.server.user.dto.request.*;
import com.yogit.server.user.dto.response.*;
import com.yogit.server.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = "User API")
public class UserController {

    private final UserService userService;

    /**
     * 유저 프로필 정보 입력 및 수정
     * @author 강신현
     */
    @ApiOperation(value = "유저 프로필 정보 입력 및 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "userName", dataTypeClass = String.class, example = "강신현"),
            @ApiImplicitParam(name = "userAge", dataTypeClass = int.class, example = "25"),
            @ApiImplicitParam(name = "gender", dataTypeClass = String.class, example = "Male"),
            @ApiImplicitParam(name = "nationality", dataTypeClass = String.class, example = "Korea"),
            @ApiImplicitParam(name = "languageCodes", dataType = "list", value = "언어 이름 코드를 순서대로 넣어주세요"),
            @ApiImplicitParam(name = "languageLevels", dataType = "list", value = "언어 레벨을 순서대로 넣어주세요"),
            @ApiImplicitParam(name = "latitude", dataTypeClass = Float.class, example = "10.23"),
            @ApiImplicitParam(name = "longitude", dataTypeClass = Float.class, example = "10.23"),
            @ApiImplicitParam(name = "aboutMe", dataTypeClass = String.class, example = "캠핑 모임에 관심이 많습니다."),
            @ApiImplicitParam(name = "job", dataTypeClass = String.class, example = "학생"),
            @ApiImplicitParam(name = "cityName", dataTypeClass = String.class, example = "SEOUL"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    })
    @PatchMapping("/profile")
    public ApplicationResponse<UserProfileRes> enterProfile(@ModelAttribute CreateUserProfileReq createUserProfileReq){
        return userService.enterProfile(createUserProfileReq);
    }

    /**
     * 유저 프로필 조회
     * @author 강신현
     */
    @ApiOperation(value = "유저 프로필 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "refreshTokenUserId", required = true, dataTypeClass = Long.class, example = "21"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    })
    @PostMapping("/profile") // iOS에서 GET 요청에 body 값을 넣을 수 없다고 하여 기존 GET 요청을 POST 요청으로 변경
    public ApplicationResponse<UserProfileRes> getProfile(@ModelAttribute GetUserProfileReq getUserProfileReq){
        return userService.getProfile(getUserProfileReq);
    }

    /**
     * 유저 사진 조회
     * @author 강신현
     */
    @ApiOperation(value = "유저 사진 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    })
    @PostMapping("/image/{userId}")
    public ApplicationResponse<UserImagesRes> getUserImage(@ModelAttribute GetUserImageReq getUserImageReq){
        return userService.getUserImage(getUserImageReq);
    }

    /**
     * 유저 사진 등록 및 삭제
     * @author 강신현
     */
    @ApiOperation(value = "유저 사진 등록 및 삭제", notes = "swagger 에서 이미지(multipartfile)처리가 잘 되지 않으므로, postman으로 테스트 바랍니다. https://solar-desert-882435.postman.co/workspace/Yogit~3e0fe8f2-15e0-41c4-9fcd-b614a975c12a/request/18177198-32a7b164-ac0b-417d-951d-46b205ac62aa")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "uploadProfileImage", dataTypeClass = MultipartFile.class, value = "새로 업로드 할 대표 프로필 이미지 - MultiPartFile"),
            @ApiImplicitParam(name = "uploadImages", dataType = "list", value = "새로 업로드 할 이외 프로필 이미지들 - List(MultiPartFile)"),
            @ApiImplicitParam(name = "deleteUserImageIds", dataType = "list", value = "삭제 할 프로필 이미지들 id - List(Long)"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    })
    @PatchMapping("/image")
    public ApplicationResponse<UserImagesRes> AddAndDeleteUserImage(@ModelAttribute AddAndDeleteUserImageReq addAndDeleteUserImageReq){
        return userService.AddAndDeleteUserImage(addAndDeleteUserImageReq);
    }

    /**
     * 유저 회원가입 (일반)
     * @author 강신현
     */
    @ApiOperation(value = "유저 회원가입", notes = "sms 인증이 완료되어야 회원가입이 가능합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginId", required = true, dataTypeClass = String.class, example = "kang123"),
            @ApiImplicitParam(name = "phoneNum", required = true, dataTypeClass = String.class, example = "01012345678")
    })
    @PostMapping("/sign-up")
    public ApplicationResponse<Void> createUser(@ModelAttribute CreateUserReq createUserReq){
        return userService.createUser(createUserReq);
    }

    /**
     * 국가 리스트 조회
     * @author 강신현
     */
    @ApiOperation(value = "국가 리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numOfRows", required = true, dataTypeClass = String.class, example = "10"),
            @ApiImplicitParam(name = "pageNo", required = true, dataTypeClass = String.class, example = "1")
    })
    @PostMapping("/nation/list") // iOS에서 GET 요청에 body 값을 넣을 수 없다고 하여 기존 GET 요청을 POST 요청으로 변경
    public ApplicationResponse<List<NationRes>> getNationList(@ModelAttribute GetNationListReq getNationListReq) {

        List<NationRes> nationListResList = null;
        try {

            URL url = new URL("http://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?ServiceKey=Os%2B%2Fa%2BWGJPptb5Rf1U850JQo11XO0fCA5cL3YND%2BxoxUm8B38IDZjHKlrpV0gj496%2Br53Rg61EdzI9KDuILDrg%3D%3D&numOfRows=" + getNationListReq.getNumOfRows() + "&pageNo=" + getNationListReq.getPageNo());

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray data = (JSONArray) jsonObject.get("data");

            nationListResList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                JSONObject nation = (JSONObject) data.get(i);

                String country_iso_alp2 = nation.get("country_iso_alp2").toString();
                String country_eng_nm = nation.get("country_eng_nm").toString();
                String download_url = nation.get("download_url").toString();

                NationRes nationRes = NationRes.create(country_iso_alp2, country_eng_nm, download_url);
                nationListResList.add(nationRes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ApplicationResponse.ok(nationListResList);
    }

    /**
     * 단일 국가 조회
     * @author 강신현
     */
    @ApiOperation(value = "단일 국가 조회 (유저 프로필 조회용)")
    @ApiImplicitParam(name = "country_iso_alp2", required = true, dataTypeClass = String.class, example = "GH")
    @PostMapping("/nation") // iOS에서 GET 요청에 body 값을 넣을 수 없다고 하여 기존 GET 요청을 POST 요청으로 변경
    public ApplicationResponse<NationRes> getNation(@ModelAttribute GetNationReq getNationReq) {

        NationRes nationRes = null;
        try {

            URL url = new URL("http://apis.data.go.kr/1262000/CountryFlagService2/getCountryFlagList2?ServiceKey=Os%2B%2Fa%2BWGJPptb5Rf1U850JQo11XO0fCA5cL3YND%2BxoxUm8B38IDZjHKlrpV0gj496%2Br53Rg61EdzI9KDuILDrg%3D%3D" + "&cond[country_iso_alp2::EQ]=" + getNationReq.getCountry_iso_alp2());

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray data = (JSONArray) jsonObject.get("data");


            JSONObject nation = (JSONObject) data.get(0);

            String country_iso_alp2 = nation.get("country_iso_alp2").toString();
            String country_eng_nm = nation.get("country_eng_nm").toString();
            String download_url = nation.get("download_url").toString();

            nationRes = NationRes.create(country_iso_alp2, country_eng_nm, download_url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ApplicationResponse.ok(nationRes);
    }

    /**
     * 디바이스 토큰 저장
     * @author 강신현
     */
    @ApiOperation(value = "디바이스 토큰 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA"),
            @ApiImplicitParam(name = "deviceToken", required = true, dataTypeClass = String.class, example = "73681e32ac3f49484fca766b8e16730a6dd6534218731619c5a1f491bedaf8df")
    })
    @PostMapping("/device-token")
    public ApplicationResponse<UserDeviceTokenRes> addDeviceToken(@ModelAttribute AddUserDeviceTokenReq addUserDeviceTokenReq){
        return userService.addDeviceToken(addUserDeviceTokenReq);
    }

    /**
     * 로그아웃
     *
     * @return
     */
    @ApiOperation(value = "로그아웃")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, dataTypeClass = Long.class, example = "1"),
            @ApiImplicitParam(name = "refreshToken", required = true, dataTypeClass = String.class, example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    })
    @PostMapping(value = "/log-out")
    @ResponseBody
    public ApplicationResponse<LogoutRes> logOut(@ModelAttribute LogoutReq logoutReq) {

        return userService.logout(logoutReq);
    }
}
