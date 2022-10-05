package com.yogit.server.user.repository;

import com.yogit.server.user.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    void deleteAllByUserId(Long userId);
}
