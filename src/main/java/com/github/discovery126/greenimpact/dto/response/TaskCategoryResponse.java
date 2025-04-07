package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskCategoryResponse {

    private Integer id;

    @JsonProperty("category_name")
    private String categoryName;
}
