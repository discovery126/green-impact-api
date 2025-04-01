package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.UserRewardResponse;
import com.github.discovery126.greenimpact.model.UserReward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRewardMapper {
    private final UserMapper userMapper;
    private final RewardMapper rewardMapper;

    public UserRewardResponse toResponse(UserReward userReward) {
        return UserRewardResponse.builder()
                .id(userReward.getId())
                .userResponse(userMapper.toResponse(userReward.getUser()))
                .rewardResponse(rewardMapper.toResponse(userReward.getReward()))
                .status(userReward.getStatus().name())
                .issuedAt(userReward.getIssuedAt())
                .build();
    }
}
