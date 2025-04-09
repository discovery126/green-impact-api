package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<List<EventResponse>>> getAllEvents() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<EventResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(eventService.getAvailableEvents())
                        .build()
                );
    }
}
