package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    List<UserReward> findAllByUserId(Long userId);
}
