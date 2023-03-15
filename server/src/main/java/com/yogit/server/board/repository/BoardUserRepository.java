package com.yogit.server.board.repository;

import com.yogit.server.board.entity.BoardUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

    @Query("select bu from BoardUser bu where bu.user.id = :userId and bu.board.id = :boardId and bu.status = 'ACTIVE'")
    Optional<BoardUser> findByUserIdAndBoardId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @Query("select bu from BoardUser bu where bu.status = 'ACTIVE' and bu.user.id = :userId and bu.board.status = 'ACTIVE'")
    Page<BoardUser> findByUserId(Pageable pageable, @Param("userId") Long userId);

    @Query("select bu from BoardUser bu where bu.status = 'ACTIVE' and bu.board.id = :boardId")
    List<BoardUser> findAllByBoardId(@Param("boardId") Long boardId);
}
