package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findAllByOrderByCostPointsAsc();
}
