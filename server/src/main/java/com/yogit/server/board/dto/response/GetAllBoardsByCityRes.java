package com.yogit.server.board.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetAllBoardsByCityRes {

    @ApiModelProperty(example = "[보드1, 보드2]", value = "보드 리스트")
    private List<GetAllBoardRes> getAllBoardResList;

    @ApiModelProperty(example = "10", value = "페이징 전체 페이지")
    private int totalPage;

    @Builder
    public GetAllBoardsByCityRes(List<GetAllBoardRes> getAllBoardResList, int totalPage) {
        this.getAllBoardResList = getAllBoardResList;
        this.totalPage = totalPage;
    }


    public static GetAllBoardsByCityRes toDto(List<GetAllBoardRes> getAllBoardResList, int totalPage){
        return GetAllBoardsByCityRes.builder()
                .getAllBoardResList(getAllBoardResList)
                .totalPage(totalPage)
                .build();
    }
}
