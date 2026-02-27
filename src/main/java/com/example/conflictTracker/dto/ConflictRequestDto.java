package com.example.conflictTracker.dto;

import com.example.conflictTracker.model.ConflictStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record ConflictRequestDto(
        @NotBlank String name,
        @NotNull LocalDate startDate,
        @NotNull ConflictStatus status,
        String description,
        Set<String> countryCodes
) {
}
