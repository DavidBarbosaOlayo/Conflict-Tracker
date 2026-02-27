package com.example.conflictTracker.dto;

import java.time.LocalDate;

public record EventResponseDto(
        Long id,
        LocalDate eventDate,
        String location,
        String description,
        Long conflictId,
        String conflictName
) {
}
