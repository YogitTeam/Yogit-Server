package com.yogit.server.report.entity;

import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.enums.ReportType;
import com.yogit.server.user.entity.Language;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "report_type")
public abstract class Report extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "reporting_user_id")
    private User reportingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "user_id", name = "reported_user_id")
    private User reportedUser;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public Report(String content, User reportingUser, User reportedUser, ReportType reportType, ReportStatus reportStatus) {
        this.content = content;
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
        this.reportType = reportType;
        this.reportStatus = reportStatus;
    }
}
