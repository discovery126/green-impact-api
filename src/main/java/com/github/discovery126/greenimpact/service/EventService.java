package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.EventRequest;
import com.github.discovery126.greenimpact.dto.request.UserEventResponse;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.exception.*;
import com.github.discovery126.greenimpact.mapper.EventMapper;
import com.github.discovery126.greenimpact.mapper.UserMapper;
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
    private final UserMapper userMapper;

    private final CityService cityService;
    private final OpenCageService openCageService;

    private final SecuritySessionContext securitySessionContext;
    private final UserRepository userRepository;
    private final UserEventRepository userEventRepository;

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
                .status(eventStatus)
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

    public List<EventResponse> getAvailableEvents() {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getStatus().equals(EventStatus.SCHEDULED))
                .map(eventMapper::toResponse)
                .toList();
    }

    public void registerEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Мероприятие с id %d не найдено".formatted(eventId)));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(eventId)));
        if (userEventRepository.existsByUserIdAndEventId(securitySessionContext.getId(),eventId)) {
            throw new UserAlreadyRegisteredEventException("Пользователь %d уже зарегистрирован"
                    .formatted(securitySessionContext.getId()));
        }
        UserEvent userEvent = UserEvent.builder()
                .user(user)
                .event(event)
                .build();

        userEventRepository.save(userEvent);
    }
    public UserEventResponse confirmEvent(Long eventId, String eventCode) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Мероприятие с id %d не найдено".formatted(eventId)));
        if (!event.getEventCode().equals(eventCode)) {
            throw new BadEventCodeException("Код неправильный");
        }
        UserEvent userEvent = userEventRepository.findByUserIdAndEventId(securitySessionContext.getId(),eventId);
        if (userEvent.getConfirmedAt() != null) {
            throw new UserAlreadyConfirmException("Вы уже подтвердили участие в мероприятие");
        }
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(eventId)));

        userEvent.setConfirmedAt(LocalDateTime.now());
        user.setPoints(user.getPoints() + event.getEventPoints());
        userEventRepository.save(userEvent);

        return UserEventResponse.builder()
                .id(userEvent.getId())
                .userResponse(userMapper.toResponse(user))
                .eventResponse(eventMapper.toResponse(event))
                .registeredAt(userEvent.getRegisteredAt())
                .confirmedAt(userEvent.getConfirmedAt())
                .build();
    }
}
