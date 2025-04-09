package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<List<RewardResponse>>> getAllRewards() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<RewardResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(rewardService.getAllAvailableRewards())
                        .build()
                );
    }
}
