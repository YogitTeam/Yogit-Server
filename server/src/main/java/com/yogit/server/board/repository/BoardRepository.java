package com.yogit.server.board.repository;

import com.yogit.server.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.id = :boardId and b.status = 'ACTIVE'")
    Optional<Board> findBoardById(@Param("boardId") Long boardId);

    @Query("select b from Board b where b.status = 'ACTIVE'")
    Slice<Board> findAllBoards(Pageable pageable);

    @Query("select b from Board b where b.status = 'ACTIVE' and b.category.id = :categoryId")
    Slice<Board> findAllBoardsByCategory(Pageable pageable, @Param("categoryId") Long categoryId);
}
