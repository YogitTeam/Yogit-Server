package com.yogit.server.report.repository;

import com.yogit.server.report.entity.BoardReport;
import com.yogit.server.report.entity.ClipBoardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipBoardReportRepository extends JpaRepository<ClipBoardReport, Long> {

    @Query("select cbr from ClipBoardReport cbr where cbr.reportingUser.id = :reportingUserId and cbr.clipBoard.id = :reportedClipBoardId and cbr.status = 'ACTIVE'")
    List<ClipBoardReport> findByReportingUserIdAndReportedClipBoardId(@Param("reportingUserId") Long reportingUserId, @Param("reportedClipBoardId") Long reportedClipBoardId);
}
