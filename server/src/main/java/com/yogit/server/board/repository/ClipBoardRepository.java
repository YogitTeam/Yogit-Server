package com.yogit.server.board.repository;


import com.yogit.server.board.entity.ClipBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClipBoardRepository extends JpaRepository<ClipBoard, Long> {

    @Query("select cb from ClipBoard cb where cb.id = :clipBoardId and cb.status = 'ACTIVE'")
    Optional<ClipBoard> findClipBoardById(@Param("clipBoardId") Long clipBoardId);


}
