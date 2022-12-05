package com.yogit.server.board.repository;

import com.yogit.server.board.entity.BoardUser;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

    @Query("select bu from BoardUser bu where bu.user.id = :userId and bu.board.id = :boardId and bu.status = 'ACTIVE'")
    Optional<BoardUser> findByUserIdAndBoardId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @Query("select bu from BoardUser bu where bu.status = 'ACTIVE' and bu.user.id = :userId")
    Slice<BoardUser> findByUserId(@Param("userId") Long userId);
}
