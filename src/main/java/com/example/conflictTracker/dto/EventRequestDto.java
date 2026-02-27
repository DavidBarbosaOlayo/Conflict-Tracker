package com.example.conflictTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventRequestDto(
        @NotNull LocalDate eventDate,
        @NotBlank String location,
        @NotBlank String description,
        @NotNull Long conflictId
) {
}
