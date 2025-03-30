package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.NotFoundOpenCageException;
import com.github.discovery126.greenimpact.exception.NotFoundOpenCageApiException;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.utils.Geometry;
import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageForwardRequest;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OpenCageService {

    private final JOpenCageGeocoder jOpenCageGeocoder;

    public OpenCageService(@Value("${open-cage.api-key}") String openCageAPIkey) {
        this.jOpenCageGeocoder = new JOpenCageGeocoder(openCageAPIkey);
        if (openCageAPIkey.isEmpty())
            throw new NotFoundOpenCageApiException("OpenCageAPI ключ отсутствует");
    }

    public Geometry getGeometryCity(String nameCity) {
        JOpenCageForwardRequest  request = new JOpenCageForwardRequest(nameCity);
        request.setRestrictToCountryCode("ru");
        request.setLanguage("ru");
        request.setAddressOnly(true);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        if (response == null)
            throw new NotFoundOpenCageException("Город с указанным названием не найден");

        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        return Geometry.builder()
                .latitude(BigDecimal.valueOf(firstResultLatLng.getLat()))
                .longitude(BigDecimal.valueOf(firstResultLatLng.getLng()))
                .build();
    }

    public Geometry getGeometryEvent(City city, String street, Integer houseNumber) {
        JOpenCageForwardRequest  request;
        if (houseNumber == null)
            request = new JOpenCageForwardRequest("%s, %s".formatted(street,city.getNameCity()));
        else
            request = new JOpenCageForwardRequest("%s %d, %s".formatted(street,houseNumber, city.getNameCity()));

        request.setRestrictToCountryCode("ru");
        request.setLanguage("ru");
        request.setAddressOnly(true);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        if (response == null)
            throw new NotFoundOpenCageException("Улица с указанным адрессом не найден");

        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        return Geometry.builder()
                .latitude(BigDecimal.valueOf(firstResultLatLng.getLat()))
                .longitude(BigDecimal.valueOf(firstResultLatLng.getLng()))
                .build();
    }
}
