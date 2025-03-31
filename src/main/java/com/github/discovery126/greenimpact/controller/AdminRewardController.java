package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.RewardRequest;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.service.RewardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/rewards")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminRewardController {

    private final RewardService rewardService;

    @GetMapping
    public ResponseEntity<List<RewardResponse>> getAllRewards() {
        return ResponseEntity
                .ok(rewardService.getAllRewards());
    }
    @PostMapping
    public ResponseEntity<RewardResponse> createReward(@RequestBody @Valid RewardRequest rewardRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rewardService.createReward(rewardRequest));
    }

}
