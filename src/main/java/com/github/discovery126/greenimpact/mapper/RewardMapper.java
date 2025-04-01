package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.dto.response.UserRewardResponse;
import com.github.discovery126.greenimpact.model.Reward;
import com.github.discovery126.greenimpact.model.UserReward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RewardMapper {

    private final UserMapper userMapper;
    public RewardResponse toResponse(Reward reward) {
        return RewardResponse.builder()
                .id(reward.getId())
                .title(reward.getTitle())
                .description(reward.getDescription())
                .costPoints(reward.getCostPoints())
                .category(reward.getCategory())
                .type(reward.getType().name())
                .amount(reward.getAmount())
                .createdAt(reward.getCreatedAt())
                .build();
    }

    public UserRewardResponse toUserRewardResponse(UserReward userReward) {
        return UserRewardResponse.builder()
                .id(userReward.getId())
                .userResponse(userMapper.toResponse(userReward.getUser()))
                .rewardResponse(toResponse(userReward.getReward()))
                .status(userReward.getStatus().name())
                .issuedAt(userReward.getIssuedAt())
                .build();
    }
}
