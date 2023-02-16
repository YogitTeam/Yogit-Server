package com.yogit.server.report.entity;

import com.yogit.server.config.domain.BaseEntity;
import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
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

//    @Enumerated(EnumType.STRING)
//    private ReportType reportType;
    private Integer reportTypeNum; // 1:Obscenity, 2:False profile, 3: Racist remarks, 4:Abusive language and slande, 5:Others

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    public Report(String content, User reportingUser, User reportedUser, Integer reportTypeNum, ReportStatus reportStatus) {
        this.content = content;
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
        this.reportTypeNum = reportTypeNum;
        this.reportStatus = reportStatus;
    }
}
