package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.RewardCategoryResponse;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.model.Reward;
import org.springframework.stereotype.Component;

@Component
public class RewardMapper {

    public RewardResponse toResponse(Reward reward) {
        return RewardResponse.builder()
                .id(reward.getId())
                .title(reward.getTitle())
                .description(reward.getDescription())
                .costPoints(reward.getCostPoints())
                .category(RewardCategoryResponse.builder()
                        .id(reward.getCategory().getId())
                        .nameCategory(reward.getCategory().getNameCategory())
                        .build())
                .type(reward.getType().name())
                .amount(reward.getAmount())
                .createdAt(reward.getCreatedAt())
                .build();
    }

}
