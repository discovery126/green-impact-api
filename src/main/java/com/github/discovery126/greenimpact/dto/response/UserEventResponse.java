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
public class UserEventResponse {
    private long id;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonProperty("event")
    private EventResponse eventResponse;

    @JsonProperty("registered_at")
    private LocalDateTime registeredAt;

    @JsonProperty("confirmed_at")
    private LocalDateTime confirmedAt;
}
