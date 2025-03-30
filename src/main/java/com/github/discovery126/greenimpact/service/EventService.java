package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.EventStatus;
import com.github.discovery126.greenimpact.dto.request.EventRequest;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.exception.EventNotFoundException;
import com.github.discovery126.greenimpact.mapper.EventMapper;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.model.Event;
import com.github.discovery126.greenimpact.repository.EventRepository;
import com.github.discovery126.greenimpact.utils.Geometry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.discovery126.greenimpact.utils.UpdateUtils.updateFieldIfChanged;

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

    public EventResponse updateEvent(EventRequest eventRequest, long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty())
            throw new EventNotFoundException("Event not found");

        City city = cityService.getCity(eventRequest.getCityId());

        Event event = eventOptional.get();

        //Если houseNumber или street или изменился, то расчитываем новую широту и долготу, иначе нет
        //houseNumber может быт null, поэтому у него другая проверка
        boolean houseNumberChanged = !Objects.equals(event.getHouseNumber(), eventRequest.getHouseNumber());
        boolean streetChanged = !event.getStreet().equals(eventRequest.getStreet());

        if (houseNumberChanged || streetChanged) {
            event.setHouseNumber(eventRequest.getHouseNumber());
            event.setStreet(eventRequest.getStreet());

            Geometry geometry = openCageService.getGeometryEvent(
                    city,
                    eventRequest.getStreet(),
                    eventRequest.getHouseNumber()
            );

            updateFieldIfChanged(event.getLatitude(), geometry.getLatitude(), event::setLatitude);
            updateFieldIfChanged(event.getLongitude(), geometry.getLongitude(), event::setLongitude);
        }

        updateFieldIfChanged(event.getName(), eventRequest.getName(), event::setName);
        updateFieldIfChanged(event.getDescription(), eventRequest.getDescription(), event::setDescription);
        updateFieldIfChanged(event.getStreet(), eventRequest.getStreet(), event::setStreet);
        updateFieldIfChanged(event.getHouseNumber(), eventRequest.getHouseNumber(), event::setHouseNumber);
        updateFieldIfChanged(event.getOrganiserName(), eventRequest.getOrganiserName(), event::setOrganiserName);
        updateFieldIfChanged(event.getOrganiserPhone(), eventRequest.getOrganiserPhone(), event::setOrganiserPhone);
        updateFieldIfChanged(event.getStartDate(), eventRequest.getStartDate(), event::setStartDate);
        updateFieldIfChanged(event.getEndDate(), eventRequest.getEndDate(), event::setEndDate);
        updateFieldIfChanged(event.getEventCode(), eventRequest.getEventCode(), event::setEventCode);
        updateFieldIfChanged(event.getEventPoints(), eventRequest.getEventPoints(), event::setEventPoints);
        updateFieldIfChanged(event.getCity(), city, event::setCity);

        return eventMapper.toResponse(eventRepository.save(event));
    }

    public void deleteEvent(long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
        } else {
            throw new EventNotFoundException("Мероприятие с id %d не найдено".formatted(eventId));
        }
    }
}
