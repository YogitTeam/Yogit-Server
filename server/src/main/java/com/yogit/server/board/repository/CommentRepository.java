package com.yogit.server.board.repository;

import com.yogit.server.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cm from Comment cm where cm.clipBoard.id = :clipBoardId and cm.status = 'ACTIVE'")
    List<Comment> findAllCommentsByClipBoardId(@Param("clipBoardId") Long clipBoardId);
}
