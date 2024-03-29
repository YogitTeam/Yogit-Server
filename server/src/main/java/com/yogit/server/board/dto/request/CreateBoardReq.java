package com.yogit.server.board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yogit.server.board.entity.Board;
import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.CityName;
import com.yogit.server.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateBoardReq {

    @ApiModelProperty(example = "SEOUL")
    @ApiParam(value = "도시 Name", required = true)
    private String cityName;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "호스트 ID", required = true)
    private Long hostId;

    @ApiModelProperty(example = "경복궁 탐사입니다.")
    @ApiParam(value = "게시글 제목", required = true)
    @Size(min = 1, max=51)
    @NotBlank
    private String title;

    @ApiModelProperty(example = "서울특별시 종로구 사직로 130")
    @ApiParam(value = "모임 주소", required = true)
    @NotBlank
    private String address;

    @ApiModelProperty(example = "강남역 3번 출구")
    @ApiParam(value = "모임 상세 주소", required = false)
    private String addressDetail;

    @ApiModelProperty(example = "37.1")
    @ApiParam(value = "위도", required = true)
    private float longitute;

    @ApiModelProperty(example = "37")
    @ApiParam(value = "경도", required = true)
    private float latitude;

    @ApiModelProperty(example = "2022-07-13 16:29:30")
    @ApiParam(value = "사용자 ID", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // form-data 형식으로 보낼 땐 @JsonFormat은 에러 남.
    private LocalDateTime date; // 모임 시각

    @ApiModelProperty(example = "시간에 맞춰오시기 바랍니다.")
    @ApiParam(value = "모임 공지사항", required = false)
    @Size(max = 1000)
    private String notice;

    @ApiModelProperty(example = "3시에 모여서 경복궁역에서 경복궁으로 출발합니다.")
    @ApiParam(value = "모임 상세설명", required = false)
    @Size(max = 3000)
    private String introduction; // 게시글 내용 상세설명

    @ApiModelProperty(example = "활발한 사람이 오면 좋습니다.")
    @ApiParam(value = "원하는 사람 설명", required = false)
    @Size(max = 3000)
    private String kindOfPerson; // 이런 사람을 원합니다 설명 글.

    @ApiModelProperty(example = "5")
    @ApiParam(value = "총 인원수", required = true)
    private int totalMember;

    @ApiModelProperty(example = "1")
    @ApiParam(value = "그룹 카테고리 ID", required = true)
    private Long categoryId;

    @ApiModelProperty(example = "\"https://yogit.s3.ap-northeast-2.amazonaws.com/boardimguuid2\"")
    @ApiParam(value = "게시글 이미지 데이터", required = false)
    private List<MultipartFile> images; // 최대 5개

    @ApiModelProperty(example = "reb5085c395164587b84ac583d023011f.0.sryrq.IDLsECw-rsTozfsX0Yz-CA")
    @ApiParam(value = "애플 리프레쉬 토큰", required = true)
    private String refreshToken;

//    public Board toEntity(CreateBoardReq dto, User host){
//        return Board.builder()
//                .title(dto.getTitle())
//                .address(dto.getAddress())
//                .longitute(dto.getLongitute())
//                .latitude(dto.getLatitude())
//                .date(dto.getDate())
//                .notice(dto.getNotice())
//                .introduction(dto.getIntroduction())
//                .kindOfPerson(dto.getKindOfPerson())
//                .totalMember(dto.getTotalMember())
//                .host(host)
//                .build();
//    }

}
