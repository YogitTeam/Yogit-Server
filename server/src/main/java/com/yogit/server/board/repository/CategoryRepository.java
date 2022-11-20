package com.yogit.server.board.repository;

import com.yogit.server.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.status = 'ACTIVE'")
    List<Category> findAllCategories();
}
