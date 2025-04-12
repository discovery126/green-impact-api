package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.UserEventResponse;
import com.github.discovery126.greenimpact.mapper.UserEventMapper;
import com.github.discovery126.greenimpact.repository.UserEventRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventService {
    private final UserEventRepository userEventRepository;
    private final UserEventMapper userEventMapper;
    private final SecuritySessionContext securitySessionContext;

    public List<UserEventResponse> getUserEvents() {
        return userEventRepository.findAllByUserIdOrderByRegisteredAtDesc(securitySessionContext.getId())
                .stream()
                .map(userEventMapper::toResponse)
                .toList();
    }
}
