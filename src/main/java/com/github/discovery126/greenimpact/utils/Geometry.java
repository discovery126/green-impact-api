package com.github.discovery126.greenimpact.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Geometry {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
