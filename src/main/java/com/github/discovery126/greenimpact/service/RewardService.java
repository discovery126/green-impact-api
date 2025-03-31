package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.RewardResponse;
import com.github.discovery126.greenimpact.mapper.RewardMapper;
import com.github.discovery126.greenimpact.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final RewardRepository rewardRepository;

    private final RewardMapper rewardMapper;

    public List<RewardResponse> getAllRewards() {
        return rewardRepository.findAll()
                .stream()
                .map(rewardMapper::toResponse)
                .toList();
    }
}
