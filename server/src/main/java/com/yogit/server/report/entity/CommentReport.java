package com.yogit.server.report.entity;

import com.yogit.server.board.entity.ClipBoard;
import com.yogit.server.board.entity.Comment;
import com.yogit.server.report.dto.req.CreateCommentReportReq;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("comment_report")
@Getter
public class CommentReport extends Report{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public CommentReport(String content, User reportingUser, User reportedUser, Integer reportTypeNum, ReportStatus reportStatus, Comment comment) {
        super(content, reportingUser, reportedUser, reportTypeNum, reportStatus);
        this.comment = comment;
    }

}
