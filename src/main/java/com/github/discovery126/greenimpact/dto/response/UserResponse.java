package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;

    private String email;

    @JsonProperty("display_name")
    private String displayName;

    private Integer points;

    private CityResponse city;

    private Set<RoleResponse> roles;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
