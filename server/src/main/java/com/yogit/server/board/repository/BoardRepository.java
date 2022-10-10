package com.yogit.server.board.repository;

import com.yogit.server.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.id = :boardId and b.status = 'ACTIVE'")
    Optional<Board> findBoardById(@Param("boardId") Long boardId);
}
