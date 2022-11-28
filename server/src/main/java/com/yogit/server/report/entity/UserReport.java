package com.yogit.server.report.entity;

import com.yogit.server.report.enums.ReportStatus;
import com.yogit.server.report.enums.ReportType;
import com.yogit.server.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("user_report")
@Getter
public class UserReport extends Report{

    @Builder
    public UserReport(String content, User reportingUser, User reportedUser, ReportType reportType, ReportStatus reportStatus) {
        super(content, reportingUser, reportedUser, reportType, reportStatus);
    }
}
