package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.RewardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponse {

    private long id;

    private String title;

    private String description;

    @JsonProperty("reward_type")
    private String type;

    private Integer amount;

    @JsonProperty("cost_points")
    private int costPoints;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private RewardCategory category;
}
