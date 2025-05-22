package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventResponse {

    private long id;

    private String title;

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

    @JsonProperty("house_number")
    private Integer houseNumber;

    private String status;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @JsonProperty("city")
    private CityResponse city;

    @JsonProperty("event_code")
    private String eventCode;
}
