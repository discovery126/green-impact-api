package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.UserRewardResponse;
import com.github.discovery126.greenimpact.mapper.RewardMapper;
import com.github.discovery126.greenimpact.repository.UserRewardRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRewardService {
    private final UserRewardRepository userRewardRepository;
    private final RewardMapper rewardMapper;
    private final SecuritySessionContext securitySessionContext;


    public List<UserRewardResponse> getUserRewards() {
        return userRewardRepository.findAllByUserId(securitySessionContext.getId())
                .stream()
                .map(rewardMapper::toUserRewardResponse)
                .toList();
    }
}
