package com.example.conflictTracker.service;

import com.example.conflictTracker.dto.ConflictResponseDto;
import com.example.conflictTracker.dto.ConflictRequestDto;
import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.repo.ConflictRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ConflictService {

    private final ConflictRepository repo;

    public ConflictService(ConflictRepository repo) {
        this.repo = repo;
    }

    public List<ConflictResponseDto> list(Conflict.States status) {
        List<Conflict> conflicts = (status == null) ? repo.findAll() : repo.findByStatus(status);
        return conflicts.stream().map(this::toDto).toList();
    }

    public ConflictResponseDto get(Long id) {
        Conflict c = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id));
        return toDto(c);
    }

    public ConflictResponseDto create(ConflictRequestDto dto) {
        Conflict c = new Conflict();
        apply(dto, c);
        Conflict saved = repo.save(c);
        return toDto(saved);
    }

    public ConflictResponseDto update(Long id, ConflictRequestDto dto) {
        Conflict c = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id));
        apply(dto, c);
        return toDto(c);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id);
        }
        repo.deleteById(id);
    }

    private void apply(ConflictRequestDto dto, Conflict c) {
        c.setName(dto.name());
        c.setStartDate(dto.startDate());
        c.setStatus(dto.status());
        c.setDescription(dto.description());
    }

    private ConflictResponseDto toDto(Conflict c) {
        return new ConflictResponseDto(c.getId(), c.getName(), c.getStartDate(), c.getStatus(), c.getDescription());
    }
}
