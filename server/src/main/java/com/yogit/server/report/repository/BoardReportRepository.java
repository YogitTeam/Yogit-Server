package com.yogit.server.report.repository;

import com.yogit.server.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
}
