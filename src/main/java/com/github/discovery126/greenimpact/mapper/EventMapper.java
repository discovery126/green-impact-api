package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {


    public EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .city(event.getCity())
                .street(event.getStreet())
                .houseNumber(event.getHouseNumber())
                .organiserName(event.getOrganiserName())
                .organiserPhone(event.getOrganiserPhone())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .eventPoints(event.getEventPoints())
                .status(event.getStatus().name())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .build();
    }
}
