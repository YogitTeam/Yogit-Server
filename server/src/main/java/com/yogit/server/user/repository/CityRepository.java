package com.yogit.server.user.repository;

import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.CityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("select c from City c where c.name = :cityName and c.status = 'ACTIVE'")
    Optional<City> findByCityName(@Param("cityName") CityName cityName);
}
