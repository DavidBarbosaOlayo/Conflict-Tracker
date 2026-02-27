package com.example.conflictTracker.service;

import com.example.conflictTracker.dto.ConflictRequestDto;
import com.example.conflictTracker.dto.ConflictResponseDto;
import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.model.ConflictStatus;
import com.example.conflictTracker.model.Country;
import com.example.conflictTracker.repo.ConflictRepository;
import com.example.conflictTracker.repo.CountryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ConflictService {

    private final ConflictRepository conflictRepository;
    private final CountryRepository countryRepository;

    public ConflictService(ConflictRepository conflictRepository, CountryRepository countryRepository) {
        this.conflictRepository = conflictRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public List<ConflictResponseDto> list(ConflictStatus status) {
        List<Conflict> conflicts = status == null
                ? conflictRepository.findAll()
                : conflictRepository.findByStatus(status);
        return conflicts.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ConflictResponseDto> listByCountryCode(String code) {
        Country country = countryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found: " + code));

        List<Conflict> conflicts = conflictRepository.findDistinctByCountries_CodeIgnoreCase(country.getCode());
        return conflicts.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ConflictResponseDto get(Long id) {
        Conflict conflict = conflictRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id));
        return toDto(conflict);
    }

    public ConflictResponseDto create(ConflictRequestDto dto) {
        Conflict conflict = new Conflict();
        apply(dto, conflict);
        return toDto(conflictRepository.save(conflict));
    }

    public ConflictResponseDto update(Long id, ConflictRequestDto dto) {
        Conflict conflict = conflictRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id));
        apply(dto, conflict);
        return toDto(conflict);
    }

    public void delete(Long id) {
        if (!conflictRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conflict not found: " + id);
        }
        conflictRepository.deleteById(id);
    }

    private void apply(ConflictRequestDto dto, Conflict conflict) {
        conflict.setName(dto.name());
        conflict.setStartDate(dto.startDate());
        conflict.setStatus(dto.status());
        conflict.setDescription(dto.description());
        conflict.setCountries(resolveCountries(dto.countryCodes()));
    }

    private Set<Country> resolveCountries(Set<String> countryCodes) {
        if (countryCodes == null || countryCodes.isEmpty()) {
            return new HashSet<>();
        }

        Set<Country> countries = new HashSet<>();
        Set<String> missingCodes = new LinkedHashSet<>();

        for (String rawCode : countryCodes) {
            if (rawCode == null || rawCode.isBlank()) {
                continue;
            }

            String code = rawCode.trim().toUpperCase();
            countryRepository.findByCodeIgnoreCase(code)
                    .ifPresentOrElse(countries::add, () -> missingCodes.add(code));
        }

        if (!missingCodes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown country codes: " + missingCodes);
        }

        return countries;
    }

    private ConflictResponseDto toDto(Conflict conflict) {
        Set<String> countryCodes = conflict.getCountries().stream()
                .map(Country::getCode)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        return new ConflictResponseDto(
                conflict.getId(),
                conflict.getName(),
                conflict.getStartDate(),
                conflict.getStatus(),
                conflict.getDescription(),
                countryCodes
        );
    }
}
