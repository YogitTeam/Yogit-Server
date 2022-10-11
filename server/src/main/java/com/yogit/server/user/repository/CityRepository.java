package com.yogit.server.user.repository;

import com.yogit.server.user.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
