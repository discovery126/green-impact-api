package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardCategoryResponse {
    private int id;

    @JsonProperty("name_category")
    private String nameCategory;
}
