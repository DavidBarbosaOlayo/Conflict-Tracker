package com.example.conflictTracker.controller;

import com.example.conflictTracker.dto.ConflictResponseDto;
import com.example.conflictTracker.dto.ConflictRequestDto;
import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.service.ConflictService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conflicts")
public class ConflictController {

    private final ConflictService service;

    public ConflictController(ConflictService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConflictResponseDto> list(@RequestParam(required = false) Conflict.States status) {
        return service.list(status);
    }

    @GetMapping("/{id}")
    public ConflictResponseDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConflictResponseDto create(@Valid @RequestBody ConflictRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ConflictResponseDto update(@PathVariable Long id, @Valid @RequestBody ConflictRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
