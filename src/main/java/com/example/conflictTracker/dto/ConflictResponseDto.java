package com.example.conflictTracker.dto;

import com.example.conflictTracker.model.ConflictStatus;

import java.time.LocalDate;
import java.util.Set;

public record ConflictResponseDto(
        Long id,
        String name,
        LocalDate startDate,
        ConflictStatus status,
        String description,
        Set<String> countryCodes
) {
}
