package com.github.discovery126.greenimpact.mapper;


import com.github.discovery126.greenimpact.dto.CityResponse;
import com.github.discovery126.greenimpact.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityResponse toResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .nameCity(city.getNameCity())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .build();
    }
    public City toEntity(CityResponse cityResponse) {
        return City.builder()
                .id(cityResponse.getId())
                .nameCity(cityResponse.getNameCity())
                .latitude(cityResponse.getLatitude())
                .longitude(cityResponse.getLongitude())
                .build();
    }
}
