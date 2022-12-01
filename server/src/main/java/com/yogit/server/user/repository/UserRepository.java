package com.yogit.server.user.repository;

import com.yogit.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);

    @Query("select u from User u where u.refreshToken = :refreshToken")
    Optional<User> findByAppleRefreshToken(@Param("refreshToken") String refreshToken);

    @Modifying
    @Query("update User u set u.reportingCnt = 0")
    void resetUserReportingCnt();

    @Query("select u from User u where u.id = :userId and u.status = 'ACTIVE'")
    Optional<User> findByUserId(@Param("userId") Long userId);
}
