package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.dto.response.UserEventResponse;
import com.github.discovery126.greenimpact.model.Event;
import com.github.discovery126.greenimpact.model.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;

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
                .eventCode(event.getEventCode())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .createdAt(event.getCreatedAt())
                .build();
    }
    public UserEventResponse toUserEventResponse(UserEvent userEvent) {
        return UserEventResponse.builder()
                .id(userEvent.getId())
                .eventResponse(toResponse(userEvent.getEvent()))
                .userResponse(userMapper.toResponse(userEvent.getUser()))
                .registeredAt(userEvent.getRegisteredAt())
                .confirmedAt(userEvent.getConfirmedAt())
                .build();

    }
}
