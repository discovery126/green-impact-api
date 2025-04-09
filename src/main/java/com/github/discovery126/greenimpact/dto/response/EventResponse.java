package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private long id;

    private String name;

    private String description;

    @JsonProperty("event_points")
    private Integer eventPoints;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @JsonProperty("organiser_name")
    private String organiserName;

    @JsonProperty("organiser_phone")
    private String organiserPhone;

    private String street;

    private Integer houseNumber;

    private String status;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @JsonProperty("city")
    private City city;
}
