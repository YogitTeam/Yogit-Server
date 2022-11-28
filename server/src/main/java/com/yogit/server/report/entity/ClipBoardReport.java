package com.yogit.server.report.entity;

import com.yogit.server.board.entity.Board;
import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.enums.ReportType;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("clip_board_report")
@Getter
public class ClipBoardReport extends Report{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clip_board_id")
    private ClipBoard clipBoard;

    @Builder
    public ClipBoardReport(String content, User reportingUser, User reportedUser, ReportType reportType, ReportStatus reportStatus, ClipBoard clipBoard) {
        super(content, reportingUser, reportedUser, reportType, reportStatus);
        this.clipBoard = clipBoard;
    }
}
