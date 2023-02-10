package com.yogit.server.user.repository;

import com.yogit.server.user.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Interest findByName(String name);

    boolean existsByName(String name);
}
