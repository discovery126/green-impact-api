package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.RatingResponse;
import com.github.discovery126.greenimpact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final UserService userService;

    @GetMapping("/week")
    public ResponseEntity<List<RatingResponse>> getRatingWeek() {
        return ResponseEntity
                .ok(userService.getRatingWeek());
    }

    @GetMapping("/month")
    public ResponseEntity<List<RatingResponse>> getRatingMonth() {
        return ResponseEntity
                .ok(userService.getRatingMonth());
    }
}
