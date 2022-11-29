package com.yogit.server.report.repository;

import com.yogit.server.report.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    @Query("select cr from CommentReport cr where cr.reportingUser.id = :reportingUserId and cr.comment.id = :reportedCommentId and cr.status = 'ACTIVE'")
    List<CommentReport> findByReportingUserIdAndReportedCommentId(@Param("reportingUserId") Long reportingUserId, @Param("reportedCommentId") Long reportedCommentId);
}
