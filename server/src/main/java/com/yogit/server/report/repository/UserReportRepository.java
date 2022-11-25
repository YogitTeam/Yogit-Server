package com.yogit.server.report.repository;

import com.yogit.server.report.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    @Query("select ur from UserReport ur where ur.reportingUser.id = :reportingUserId and ur.reportedUser.id = :reportedUserId and ur.status = 'ACTIVE'")
    List<UserReport> findByReportingUserIdAndReportedUserId(@Param("reportingUserId") Long reportingUserId, @Param("reportedUserId") Long reportedUserId);
}
