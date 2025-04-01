package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.UserEventResponse;
import com.github.discovery126.greenimpact.model.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public UserEventResponse toResponse(UserEvent userEvent) {
        return UserEventResponse.builder()
                .id(userEvent.getId())
                .userResponse(userMapper.toResponse(userEvent.getUser()))
                .eventResponse(eventMapper.toResponse(userEvent.getEvent()))
                .registeredAt(userEvent.getRegisteredAt())
                .confirmedAt(userEvent.getConfirmedAt())
                .build();

    }
}
