package com.example.conflictTracker.controller;

import com.example.conflictTracker.dto.FactionRequestDto;
import com.example.conflictTracker.dto.FactionResponseDto;
import com.example.conflictTracker.service.FactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factions")
public class FactionController {

    private final FactionService factionService;

    public FactionController(FactionService factionService) {
        this.factionService = factionService;
    }

    @GetMapping
    public List<FactionResponseDto> list() {
        return factionService.list();
    }

    @GetMapping("/{id}")
    public FactionResponseDto get(@PathVariable Long id) {
        return factionService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FactionResponseDto create(@Valid @RequestBody FactionRequestDto dto) {
        return factionService.create(dto);
    }

    @PutMapping("/{id}")
    public FactionResponseDto update(@PathVariable Long id, @Valid @RequestBody FactionRequestDto dto) {
        return factionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        factionService.delete(id);
    }
}
