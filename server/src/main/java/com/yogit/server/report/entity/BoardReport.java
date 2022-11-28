package com.yogit.server.report.entity;

import com.yogit.server.board.entity.Board;
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
@DiscriminatorValue("board_report")
@Getter
public class BoardReport extends Report{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardReport(String content, User reportingUser, User reportedUser, ReportType reportType, ReportStatus reportStatus, Board board) {
        super(content, reportingUser, reportedUser, reportType, reportStatus);
        this.board = board;
    }
}
