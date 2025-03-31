package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.RewardRequest;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.mapper.RewardMapper;
import com.github.discovery126.greenimpact.model.Reward;
import com.github.discovery126.greenimpact.model.RewardCategory;
import com.github.discovery126.greenimpact.model.RewardType;
import com.github.discovery126.greenimpact.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final RewardRepository rewardRepository;
    private final RewardCategoryService rewardCategoryService;

    private final RewardMapper rewardMapper;

    public List<RewardResponse> getAllRewards() {
        return rewardRepository.findAll()
                .stream()
                .map(rewardMapper::toResponse)
                .toList();
    }

    public RewardResponse createReward(RewardRequest rewardRequest) {
        RewardCategory rewardCategory = rewardCategoryService.getRewardCategory(rewardRequest.getCategoryId());
        RewardType rewardType = RewardType.valueOf(rewardRequest.getRewardType().toUpperCase());

        Reward reward = Reward.builder()
                .title(rewardRequest.getTitle())
                .description(rewardRequest.getDescription())
                .costPoints(rewardRequest.getCategoryId())
                .amount(rewardRequest.getAmount())
                .type(rewardType)
                .category(rewardCategory)
                .build();

        return rewardMapper.toResponse(rewardRepository.save(reward));
    }
}
