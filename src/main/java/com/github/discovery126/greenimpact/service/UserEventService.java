package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.UserEventResponse;
import com.github.discovery126.greenimpact.mapper.EventMapper;
import com.github.discovery126.greenimpact.repository.UserEventRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventService {
    private final EventMapper eventMapper;
    private final UserEventRepository userEventRepository;
    private final SecuritySessionContext securitySessionContext;

    public List<UserEventResponse> getUserEvents() {
        return userEventRepository.findAllByUserId(securitySessionContext.getId())
                .stream()
                .map(eventMapper::toUserEventResponse)
                .toList();
    }
}
