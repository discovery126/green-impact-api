package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
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
    public ResponseEntity<BaseSuccessResponse<List<RewardResponse>>> getAllRewards() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<RewardResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(rewardService.getAllRewards())
                        .build()
                );
    }
    @PostMapping
    public ResponseEntity<BaseSuccessResponse<RewardResponse>> createReward(@RequestBody @Valid RewardRequest rewardRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseSuccessResponse.<RewardResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(rewardService.createReward(rewardRequest))
                        .build()
                );
    }

    @PostMapping("{id}")
    public ResponseEntity<BaseSuccessResponse<RewardResponse>> updateReward(@RequestBody @Valid RewardRequest rewardRequest,
                                                       @PathVariable Long id) {
        return ResponseEntity
                .ok(BaseSuccessResponse.<RewardResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(rewardService.updateReward(rewardRequest,id))
                        .build()
                );
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
