package com.yogit.server.board.repository;

import com.yogit.server.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    @Query("select bi from BoardImage bi where bi.id=:boardImageId and bi.status='ACTIVE'")
    Optional<BoardImage> findBoardImageById(@Param("boardImageId") Long boardImageId);

}
