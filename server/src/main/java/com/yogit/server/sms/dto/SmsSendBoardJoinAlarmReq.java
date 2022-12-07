//package com.yogit.server.sms.dto;
//
//import com.yogit.server.user.entity.User;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@ApiModel(description = "아이디 중복 확인을 위한 요청 객체")
//public class SmsSendBoardJoinAlarmReq {
//
//    @NotBlank(message = "전화번호를 입력해 주세요.")
//    @Size(min = 11, max = 13, message = "전화번호를 확인하여 주세요.")
//    @ApiModelProperty(notes = "전화번호를 입력해 주세요.")
//    private String phoneNum;
//
//    @ApiModelProperty(example = "Hong Gil Dong")
//    @ApiParam(value = "유저 Name", required = true)
//    private String userName;
//
//    public static SmsSendReq from(User user) {
//        return SmsSendReq.builder()
//                .phoneNum(user.getPhoneNum())
//                .build();
//    }
//}
