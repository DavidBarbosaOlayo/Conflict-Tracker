package com.example.conflictTracker.dto;

import com.example.conflictTracker.model.Conflict;

import java.time.LocalDate;

public record ConflictResponseDto(Long id, String name, LocalDate startDate, Conflict.States status,
                                  String description) {
}
