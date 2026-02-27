package com.example.conflictTracker.dto;

import java.util.Set;

public record FactionResponseDto(
        Long id,
        String name,
        Long conflictId,
        String conflictName,
        Set<String> supportedCountryCodes
) {
}
