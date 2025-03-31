package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.RewardCategoryNotFoundException;
import com.github.discovery126.greenimpact.model.RewardCategory;
import com.github.discovery126.greenimpact.repository.RewardCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardCategoryService {
    private final RewardCategoryRepository rewardCategoryRepository;

    public RewardCategory getRewardCategory(Integer rewardCategoryId) {
        Optional<RewardCategory> rewardCategory = rewardCategoryRepository.findById(rewardCategoryId);
        if (rewardCategory.isEmpty())
            throw new RewardCategoryNotFoundException("Категория награды с %d не найдена"
                    .formatted(rewardCategoryId));

        return rewardCategory.get();
    }


}
