package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
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
    public ResponseEntity<BaseSuccessResponse<EventResponse>> createEvent(@RequestBody @Valid EventRequest eventRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseSuccessResponse.<EventResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(eventService.createEvent(eventRequest))
                        .build()
                );
    }
    @GetMapping
    public ResponseEntity<BaseSuccessResponse<List<EventResponse>>> getAllEvents() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<EventResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(eventService.getAllEvents())
                        .build()
                );
    }

    @PostMapping("{id}")
    public ResponseEntity<BaseSuccessResponse<EventResponse>> updateEvent(@RequestBody @Valid EventRequest eventRequest,
                                                     @PathVariable long id) {
        return ResponseEntity
                .ok(BaseSuccessResponse.<EventResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(eventService.updateEvent(eventRequest,id))
                        .build()
                );
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventService.deleteEvent(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
