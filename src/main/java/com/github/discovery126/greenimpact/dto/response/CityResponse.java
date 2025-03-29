package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {

    private int id;

    @JsonProperty("name_city")
    private String nameCity;

    private BigDecimal latitude;

    private BigDecimal longitude;
}
