package com.yogit.server.board.dto.response.clipboard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetClipBoardsRes {

    @ApiModelProperty(example = "[클리보드1, 클립보드2]", value = "클립 보드 리스트")
    private List<GetClipBoardRes> getClipBoardResList;

    @ApiModelProperty(example = "10", value = "페이징 전체 페이지")
    private int totalPage;

    @Builder
    public GetClipBoardsRes(List<GetClipBoardRes> getClipBoardResList, int totalPage) {
        this.getClipBoardResList = getClipBoardResList;
        this.totalPage = totalPage;
    }

    public static GetClipBoardsRes toDto(List<GetClipBoardRes> getClipBoardResList, int totalPage) {
        return GetClipBoardsRes.builder()
                .getClipBoardResList(getClipBoardResList)
                .totalPage(totalPage)
                .build();
    }
}
