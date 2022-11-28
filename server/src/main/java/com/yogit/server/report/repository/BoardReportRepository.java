package com.yogit.server.report.repository;

import com.yogit.server.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    @Query("select br from BoardReport br where br.reportingUser.id = :reportingUserId and br.board.id = :reportedBoardId and br.status = 'ACTIVE'")
    List<BoardReport> findByReportingUserIdAndReportedBoardId(@Param("reportingUserId") Long reportingUserId, @Param("reportedBoardId") Long reportedBoardId);
}
