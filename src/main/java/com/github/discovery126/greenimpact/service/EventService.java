package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.EventStatus;
import com.github.discovery126.greenimpact.dto.request.EventRequest;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.mapper.EventMapper;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.model.Event;
import com.github.discovery126.greenimpact.repository.EventRepository;
import com.github.discovery126.greenimpact.utils.Geometry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final CityService cityService;
    private final OpenCageService openCageService;

    public EventResponse createEvent(EventRequest eventRequest) {
        City city = cityService.getCity(eventRequest.getCityId());
        Geometry geometry = openCageService
                .getGeometryEvent(city, eventRequest.getStreet(),eventRequest.getHouseNumber());
        EventStatus eventStatus = EventStatus.SCHEDULED;

        Event event = Event.builder()
                .name(eventRequest.getName())
                .description(eventRequest.getDescription())
                .city(city)
                .street(eventRequest.getStreet())
                .houseNumber(eventRequest.getHouseNumber())
                .organiserName(eventRequest.getOrganiserName())
                .organiserPhone(eventRequest.getOrganiserPhone())
                .startDate(eventRequest.getStartDate())
                .endDate(eventRequest.getEndDate())
                .eventPoints(eventRequest.getEventPoints())
                .status(eventStatus.name().toLowerCase())
                .eventCode(eventRequest.getEventCode())
                .latitude(geometry.getLatitude())
                .longitude(geometry.getLongitude())
                .build();

        return eventMapper.toResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }
}
