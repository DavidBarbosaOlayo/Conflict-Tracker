package com.example.conflictTracker.dto;

import com.example.conflictTracker.model.Conflict;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ConflictRequestDto(@NotBlank String name, @NotNull LocalDate startDate, @NotNull Conflict.States status,
                                 String description) {
}
