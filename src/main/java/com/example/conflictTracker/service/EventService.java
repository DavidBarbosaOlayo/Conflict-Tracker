package com.example.conflictTracker.service;

import com.example.conflictTracker.dto.EventRequestDto;
import com.example.conflictTracker.dto.EventResponseDto;
import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.model.Event;
import com.example.conflictTracker.repo.ConflictRepository;
import com.example.conflictTracker.repo.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final ConflictRepository conflictRepository;

    public EventService(EventRepository eventRepository, ConflictRepository conflictRepository) {
        this.eventRepository = eventRepository;
        this.conflictRepository = conflictRepository;
    }

    @Transactional(readOnly = true)
    public List<EventResponseDto> list() {
        return eventRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public EventResponseDto get(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id));
        return toDto(event);
    }

    public EventResponseDto create(EventRequestDto dto) {
        Event event = new Event();
        apply(dto, event);
        return toDto(eventRepository.save(event));
    }

    public EventResponseDto update(Long id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id));
        apply(dto, event);
        return toDto(event);
    }

    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id);
        }
        eventRepository.deleteById(id);
    }

    private void apply(EventRequestDto dto, Event event) {
        Conflict conflict = conflictRepository.findById(dto.conflictId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conflict not found: " + dto.conflictId()));

        event.setEventDate(dto.eventDate());
        event.setLocation(dto.location());
        event.setDescription(dto.description());
        event.setConflict(conflict);
    }

    private EventResponseDto toDto(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getEventDate(),
                event.getLocation(),
                event.getDescription(),
                event.getConflict().getId(),
                event.getConflict().getName()
        );
    }
}
