package com.yogit.server.block.repository;

import com.yogit.server.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("select bl from Block bl where bl.blockingUser.id = :blockingUserId and bl.blockedUser.id = :blockedUserId and bl.status = 'ACTIVE'")
    List<Block> findByBlockingUserIdAndBlockedUserId(@Param("blockingUserId") Long blockingUserId, @Param("blockedUserId") Long blockedUserId);

    @Query("select bl from Block bl where bl.blockingUser.id = :blockingUserId and bl.status = 'ACTIVE'")
    List<Block> findBlocksByBlockingUserId(@Param("blockingUserId") Long blockingUserId);

    boolean existsByBlockingUserIdAndBlockedUserId(Long blockingUserId, Long blockedUserId);
}
