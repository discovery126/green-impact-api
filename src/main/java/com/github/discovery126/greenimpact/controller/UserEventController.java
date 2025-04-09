package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.UserEventResponse;
import com.github.discovery126.greenimpact.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user/events")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class UserEventController {
    private final EventService eventService;

    @PostMapping("/{eventId}/register")
    public ResponseEntity<Void> registerEvent(@PathVariable Long eventId) {
        eventService.registerEvent(eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @PostMapping("/{eventId}/confirm")
    public ResponseEntity<UserEventResponse> confirmEvent(@PathVariable Long eventId,
                                                          @RequestParam String eventCode) {
        eventService.confirmEvent(eventId,eventCode);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
