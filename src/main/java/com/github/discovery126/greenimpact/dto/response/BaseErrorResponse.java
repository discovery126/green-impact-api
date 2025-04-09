package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseErrorResponse implements BaseResponse {
    private int code;
    private List<String> message;

    @Override
    public int getCode() {
        return code;
    }
}
