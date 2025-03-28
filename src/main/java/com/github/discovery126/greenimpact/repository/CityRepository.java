package com.github.discovery126.greenimpact.repository;


import com.github.discovery126.greenimpact.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNameCity(String name);
}
