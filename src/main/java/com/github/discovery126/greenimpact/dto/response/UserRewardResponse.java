package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardResponse {

    private long id;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonProperty("reward")
    private RewardResponse rewardResponse;

    @JsonProperty("promo_code")
    private String promoCode;

    private String status;

    @JsonProperty("issued_at")
    private LocalDateTime issuedAt;
}
