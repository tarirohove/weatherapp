package com.vanguard.repository;

import com.vanguard.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    Optional<WeatherData> findFirstByNameOrderByIdDesc(String city);
}

