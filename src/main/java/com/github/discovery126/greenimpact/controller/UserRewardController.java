package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/rewards")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class UserRewardController {
    private final RewardService rewardService;

    @PostMapping("/{rewardId}/exchange")
    public ResponseEntity<Void> exchangeReward(@PathVariable Long rewardId) {
        rewardService.exchangeReward(rewardId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
