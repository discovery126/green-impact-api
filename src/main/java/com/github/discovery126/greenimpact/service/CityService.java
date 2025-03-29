package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.CityRequest;
import com.github.discovery126.greenimpact.dto.response.CityResponse;
import com.github.discovery126.greenimpact.exception.CityNotFoundException;
import com.github.discovery126.greenimpact.mapper.CityMapper;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.repository.CityRepository;
import com.github.discovery126.greenimpact.utils.Geometry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final OpenCageService openCageService;

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

    public List<CityResponse> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(cityMapper::toResponse)
                .toList();
    }

    public CityResponse createCity(CityRequest cityRequest) {
        Geometry geometry = openCageService.getGeometryCity(cityRequest.getNameCity());
        City city = City.builder()
                .latitude(geometry.getLatitude())
                .longitude(geometry.getLongitude())
                .nameCity(cityRequest.getNameCity())
                .build();

        return cityMapper.toResponse(cityRepository.save(city));
    }

    public void deleteCity(Integer cityId) {
        if (cityRepository.existsById(cityId)) {
            cityRepository.deleteById(cityId);
        } else {
            throw new CityNotFoundException("Город с id %d не найден".formatted(cityId));
        }
    }
}
