package com.yogit.server.board.repository;

import com.yogit.server.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select cm from Comment cm where cm.clipBoard.id = :clipBoardId and cm.status = 'ACTIVE'")
    List<Comment> findAllCommentsByClipBoardId(@Param("clipBoardId") Long clipBoardId);

    @Query("select cm from Comment cm where cm.id = :commentId and cm.status='ACTIVE'")
    Optional<Comment> findCommentById(@Param("commentId") Long commentId);
}
