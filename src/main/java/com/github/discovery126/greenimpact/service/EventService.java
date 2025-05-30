package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.EventRequest;
import com.github.discovery126.greenimpact.dto.response.AdminEventResponse;
import com.github.discovery126.greenimpact.dto.response.BooleanResponse;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.mapper.EventMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.EventRepository;
import com.github.discovery126.greenimpact.repository.UserEventRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import com.github.discovery126.greenimpact.utils.Geometry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final SecuritySessionContext securitySessionContext;
    private final UserRepository userRepository;
    private final UserEventRepository userEventRepository;

    public AdminEventResponse createEvent(EventRequest eventRequest) {
        City city = cityService.getCity(eventRequest.getCityId());
        Geometry geometry = openCageService
                .getGeometryEvent(city, eventRequest.getStreet(),eventRequest.getHouseNumber());
        EventStatus eventStatus = EventStatus.SCHEDULED;

        Event event = Event.builder()
                .title(eventRequest.getTitle())
                .description(eventRequest.getDescription())
                .city(city)
                .street(eventRequest.getStreet())
                .houseNumber(eventRequest.getHouseNumber())
                .organiserName(eventRequest.getOrganiserName())
                .organiserPhone(eventRequest.getOrganiserPhone())
                .startDate(eventRequest.getStartDate())
                .endDate(eventRequest.getEndDate())
                .eventPoints(eventRequest.getEventPoints())
                .status(eventStatus)
                .eventCode(eventRequest.getEventCode())
                .latitude(geometry.getLatitude())
                .longitude(geometry.getLongitude())
                .build();

        return eventMapper.toAdminEventResponse(eventRepository.save(event));
    }

    public List<AdminEventResponse> getAllEvents() {
        return eventRepository.findAllByOrderByStartDateDesc()
                .stream()
                .map(eventMapper::toAdminEventResponse)
                .toList();
    }

    public AdminEventResponse updateEvent(EventRequest eventRequest, long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty())
            throw new CustomException(ValidationConstants.EVENT_ID_NOT_FOUND);

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

        updateFieldIfChanged(event.getTitle(), eventRequest.getTitle(), event::setTitle);
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

        return eventMapper.toAdminEventResponse(eventRepository.save(event));
    }

    public void deleteEvent(long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
        } else {
            throw new CustomException(ValidationConstants.EVENT_ID_NOT_FOUND);
        }
    }

    public List<EventResponse> getAvailableEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getStatus().equals(EventStatus.SCHEDULED))
                .map(eventMapper::toResponse)
                .toList();
    }

    public void registerEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(ValidationConstants.EVENT_ID_NOT_FOUND));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new CustomException(ValidationConstants.USER_NOT_FOUND));
        if (userEventRepository.existsByUserIdAndEventId(securitySessionContext.getId(),eventId)) {
            throw new CustomException(ValidationConstants.EVENT_USER_ALREADY_REGISTERED);
        }
        UserEvent userEvent = UserEvent.builder()
                .user(user)
                .event(event)
                .build();

        userEventRepository.save(userEvent);
    }
    public void confirmEvent(Long eventId, String eventCode) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(ValidationConstants.EVENT_ID_NOT_FOUND));
        if (event.getStatus()!=EventStatus.ACTIVE) {
            throw new CustomException(ValidationConstants.EVENT_NOT_ACTIVE);
        }
        if (!event.getEventCode().equals(eventCode)) {
            throw new CustomException(ValidationConstants.EVENT_BAD_CODE);
        }
        UserEvent userEvent = userEventRepository.findByUserIdAndEventId(securitySessionContext.getId(),eventId);
        if (userEvent.getConfirmedAt() != null) {
            throw new CustomException(ValidationConstants.EVENT_USER_ALREADY_CONFIRM);
        }
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new CustomException(ValidationConstants.USER_NOT_FOUND));

        userEvent.setConfirmedAt(LocalDateTime.now());
        user.setPoints(user.getPoints() + event.getEventPoints());
        userEventRepository.save(userEvent);
    }

    public BooleanResponse isUserRegisteredForEvent(Long eventId) {
        return new BooleanResponse(userEventRepository.existsByUserIdAndEventId(securitySessionContext.getId(),eventId));
    }

    public BooleanResponse isUserConfirmedEvent(Long eventId) {
        return new BooleanResponse(userEventRepository.existsByUserIdAndEventIdAndConfirmedAtNotNull(securitySessionContext.getId(),eventId));
    }
}
