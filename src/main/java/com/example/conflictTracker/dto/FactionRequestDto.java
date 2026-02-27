package com.example.conflictTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record FactionRequestDto(
        @NotBlank String name,
        @NotNull Long conflictId,
        Set<String> supportedCountryCodes
) {
}
