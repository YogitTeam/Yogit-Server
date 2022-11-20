package com.yogit.server.user.repository;

import com.yogit.server.user.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);
}
