package com.yogit.server.user.repository;

import com.yogit.server.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    List<UserInterest> findAllByUserId(Long userId);

    boolean existsByUserIdAndInterestId(Long userId, Long interestId);
}
