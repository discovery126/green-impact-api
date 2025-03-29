package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.CityNotFoundException;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City getCity(String nameCity) {
        Optional<City> city = cityRepository.findByNameCity(nameCity);
        if (city.isEmpty())
            throw new CityNotFoundException("Город с названием %s не найден".formatted(nameCity));

        return city.get();
    }

    public City getCity(Integer id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isEmpty())
            throw new CityNotFoundException("Город с id %d не найден".formatted(id));

        return city.get();
    }


}
