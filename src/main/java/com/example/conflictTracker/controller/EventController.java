package com.example.conflictTracker.controller;

import com.example.conflictTracker.dto.EventRequestDto;
import com.example.conflictTracker.dto.EventResponseDto;
import com.example.conflictTracker.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventResponseDto> list() {
        return eventService.list();
    }

    @GetMapping("/{id}")
    public EventResponseDto get(@PathVariable Long id) {
        return eventService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto create(@Valid @RequestBody EventRequestDto dto) {
        return eventService.create(dto);
    }

    @PutMapping("/{id}")
    public EventResponseDto update(@PathVariable Long id, @Valid @RequestBody EventRequestDto dto) {
        return eventService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }
}
