package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.EventRequest;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/events")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(eventRequest));
    }
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity
                .ok(eventService.getAllEvents());
    }

    @PostMapping("{id}")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody @Valid EventRequest eventRequest,
                                                     @PathVariable long id) {
        return ResponseEntity
                .ok(eventService.updateEvent(eventRequest,id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventService.deleteEvent(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
