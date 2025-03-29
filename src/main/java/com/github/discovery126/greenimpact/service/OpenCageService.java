package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.NotFoundCityOpenCageException;
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
    }

    public Geometry getGeometryCity(String nameCity) {
        JOpenCageForwardRequest  request = new JOpenCageForwardRequest(nameCity);
        request.setRestrictToCountryCode("ru");
        request.setLanguage("ru");
        request.setAddressOnly(true);

        JOpenCageResponse response = jOpenCageGeocoder.forward(request);

        if (response == null)
            throw new NotFoundCityOpenCageException("Город с указанным названием не найден");

        JOpenCageLatLng firstResultLatLng = response.getFirstPosition();
        return Geometry.builder()
                .latitude(BigDecimal.valueOf(firstResultLatLng.getLat()))
                .longitude(BigDecimal.valueOf(firstResultLatLng.getLng()))
                .build();
    }
}
