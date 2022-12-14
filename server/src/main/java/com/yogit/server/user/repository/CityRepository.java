package com.yogit.server.user.repository;

import com.yogit.server.user.entity.City;
import com.yogit.server.user.entity.CityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    // 프론트에서 넘겨주는 방식으로 변경 -> CityName enum 사용하지 않고 입력 받은 String을 곧바로 저장
    // map 관련 프레임워크로 받은 도시 이름으로 요청하기로 했기 때문에, validation은 염두하지 않아도 됨
    City findByCityName(String cityName);

    Boolean existsByCityName(String cityName);
}
