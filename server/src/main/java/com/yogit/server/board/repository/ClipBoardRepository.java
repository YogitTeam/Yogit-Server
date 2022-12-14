package com.yogit.server.board.repository;


import com.yogit.server.board.entity.ClipBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClipBoardRepository extends JpaRepository<ClipBoard, Long> {

    @Query("select cb from ClipBoard cb where cb.id = :clipBoardId and cb.status = 'ACTIVE'")
    Optional<ClipBoard> findClipBoardById(@Param("clipBoardId") Long clipBoardId);

    @Query("select cb from ClipBoard cb where cb.board.id = :boardId and cb.status = 'ACTIVE'")
    List<ClipBoard> findAllByBoardId(@Param("boardId") Long boardId);

    @Query("select cb from ClipBoard cb where cb.status = 'ACTIVE' and cb.board.id = :boardId")
    Slice<ClipBoard> findClipBoardsByBoardId(Pageable pageable, @Param("boardId") Long boardId);

    @Query("select cb from ClipBoard cb where cb.status = 'ACTIVE' and cb.board.id = :boardId")
    Page<ClipBoard> findClipBoardsByBoardIdWithTotalPage(Pageable pageable, @Param("boardId") Long boardId);
}
