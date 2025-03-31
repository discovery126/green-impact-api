package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.RewardRequest;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.exception.RewardNotFoundException;
import com.github.discovery126.greenimpact.mapper.RewardMapper;
import com.github.discovery126.greenimpact.model.Reward;
import com.github.discovery126.greenimpact.model.RewardCategory;
import com.github.discovery126.greenimpact.model.RewardType;
import com.github.discovery126.greenimpact.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.github.discovery126.greenimpact.utils.UpdateUtils.updateFieldIfChanged;

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

    public RewardResponse updateReward(RewardRequest rewardRequest, Long rewardId) {
        Optional<Reward> rewardOptional = rewardRepository.findById(rewardId);
        if (rewardOptional.isEmpty()) {
            throw new RewardNotFoundException("Награда с id %d не найдена".formatted(rewardId));
        }
        RewardCategory rewardCategory = rewardCategoryService.getRewardCategory(rewardRequest.getCategoryId());
        RewardType rewardType = RewardType.valueOf(rewardRequest.getRewardType().toUpperCase());

        Reward reward = rewardOptional.get();

        updateFieldIfChanged(reward.getTitle(),rewardRequest.getTitle(), reward::setTitle);
        updateFieldIfChanged(reward.getDescription(),rewardRequest.getDescription(), reward::setDescription);
        updateFieldIfChanged(reward.getAmount(),rewardRequest.getAmount(), reward::setAmount);
        updateFieldIfChanged(reward.getCostPoints(), rewardRequest.getCostPoints(), reward::setCostPoints);
        updateFieldIfChanged(reward.getCategory(),rewardCategory, reward::setCategory);
        updateFieldIfChanged(reward.getType(),rewardType, reward::setType);

        return rewardMapper.toResponse(rewardRepository.save(reward));
    }
}
