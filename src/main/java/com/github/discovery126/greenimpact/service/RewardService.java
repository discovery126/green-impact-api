package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.RewardRequest;
import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.exception.NotEnoughPointsException;
import com.github.discovery126.greenimpact.exception.RewardNotFoundException;
import com.github.discovery126.greenimpact.exception.RewardOutOfStockException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.mapper.RewardMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.RewardRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.repository.UserRewardRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
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
    private final UserRepository userRepository;

    private final SecuritySessionContext securitySessionContext;
    private final PromoCodeService promoCodeService;
    private final UserRewardRepository userRewardRepository;

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

    public void deleteReward(Long rewardId) {
        if (rewardRepository.existsById(rewardId)) {
            rewardRepository.deleteById(rewardId);
        } else {
            throw new UserNotFoundException("Награда с id %d не найдена".formatted(rewardId));
        }
    }
    public void exchangeReward(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new UserNotFoundException("Награда с id %d не найдено".formatted(rewardId)));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(rewardId)));
        if (reward.getAmount().equals(0)) {
            throw new RewardOutOfStockException("Награды данного типа закончились");
        }
        if (user.getPoints()<reward.getCostPoints()) {
            throw new NotEnoughPointsException("Не хватает баллов для обмена");
        }
        String promoCode = promoCodeService.generatePromoCode();
        UserReward userReward = UserReward.builder()
                .user(user)
                .reward(reward)
                .promoCode(promoCode)
                .status(UserRewardStatus.REDEEMED)
                .build();

        user.setPoints(user.getPoints()-reward.getCostPoints());
        reward.setAmount(reward.getAmount()-1);

        rewardRepository.save(reward);
        userRewardRepository.save(userReward);
    }

    public List<RewardResponse> getAllAvailableRewards() {
        return rewardRepository.findAll()
                .stream()
                .filter(reward -> reward.getAmount() > 0)
                .map(rewardMapper::toResponse)
                .toList();
    }
}
