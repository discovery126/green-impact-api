package com.github.discovery126.greenimpact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_rewards")
public class UserReward {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reward_id", referencedColumnName = "id", nullable = false)
    private Reward reward;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRewardStatus status;

    @Column(name = "issued_at")
    @Builder.Default
    private LocalDateTime issuedAt = LocalDateTime.now();
}
