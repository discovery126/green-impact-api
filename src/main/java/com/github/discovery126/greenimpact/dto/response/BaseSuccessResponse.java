package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseSuccessResponse<T> implements BaseResponse {
    private int code;
    private T data;

    @Override
    public int getCode() {
        return code;
    }
}