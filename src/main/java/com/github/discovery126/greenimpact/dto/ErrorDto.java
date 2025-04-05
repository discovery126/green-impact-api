package com.github.discovery126.greenimpact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ErrorDto(@JsonProperty("error_details") List<String> errorDetails) {
}
