package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.model.Reward;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.model.UserReward;
import com.github.discovery126.greenimpact.repository.RewardRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.repository.UserRewardRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @InjectMocks
    private RewardService rewardService;

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRewardRepository userRewardRepository;

    @Mock
    private PromoCodeService promoCodeService;

    @Mock
    private SecuritySessionContext securitySessionContext;

    @Test
    void shouldExchangeRewardSuccessfully() {
        Long rewardId = 1L;
        Long userId = 42L;

        Reward reward = Reward.builder()
                .id(rewardId)
                .costPoints(100)
                .amount(5)
                .build();

        User user = User.builder()
                .id(userId)
                .points(150)
                .build();

        String generatedPromoCode = "PROMO123";

        when(securitySessionContext.getId()).thenReturn(userId);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(promoCodeService.generatePromoCode()).thenReturn(generatedPromoCode);

        rewardService.exchangeReward(rewardId);

        assertEquals(50, user.getPoints());
        assertEquals(4, reward.getAmount());

        verify(securitySessionContext).getId();
        verify(rewardRepository).findById(rewardId);
        verify(userRepository).findById(userId);
        verify(promoCodeService).generatePromoCode();
        verify(userRewardRepository).save(any(UserReward.class));
        verify(rewardRepository).save(reward);
    }
    @Test
    void shouldThrowExceptionWhenRewardOutOfStock() {
        Long rewardId = 1L;
        Long userId = 42L;

        Reward reward = Reward.builder()
                .id(rewardId)
                .costPoints(100)
                .amount(0)
                .build();

        User user = User.builder()
                .id(userId)
                .points(150)
                .build();

        when(securitySessionContext.getId()).thenReturn(userId);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () ->
                rewardService.exchangeReward(rewardId)
        );

        assertEquals(ValidationConstants.REWARD_OUT_OF_STOCK, exception.getMessage());

        verify(rewardRepository).findById(rewardId);
        verify(userRepository).findById(userId);
        verify(securitySessionContext).getId();
        verifyNoMoreInteractions(promoCodeService, userRewardRepository, rewardRepository);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotHaveEnoughPoints() {
        Long rewardId = 1L;
        Long userId = 42L;

        Reward reward = Reward.builder()
                .id(rewardId)
                .costPoints(100)
                .amount(3)
                .build();

        User user = User.builder()
                .id(userId)
                .points(50)
                .build();

        when(securitySessionContext.getId()).thenReturn(userId);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () ->
                rewardService.exchangeReward(rewardId)
        );

        assertEquals(ValidationConstants.REWARD_NOT_ENOUGH_POINTS, exception.getMessage());

        verify(rewardRepository).findById(rewardId);
        verify(userRepository).findById(userId);
        verify(securitySessionContext).getId();
        verifyNoMoreInteractions(promoCodeService, userRewardRepository, rewardRepository);
    }

}