package com.example.conflictTracker.service;

import com.example.conflictTracker.dto.FactionRequestDto;
import com.example.conflictTracker.dto.FactionResponseDto;
import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.model.Country;
import com.example.conflictTracker.model.Faction;
import com.example.conflictTracker.repo.ConflictRepository;
import com.example.conflictTracker.repo.CountryRepository;
import com.example.conflictTracker.repo.FactionRepository;
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
public class FactionService {

    private final FactionRepository factionRepository;
    private final ConflictRepository conflictRepository;
    private final CountryRepository countryRepository;

    public FactionService(FactionRepository factionRepository,
                          ConflictRepository conflictRepository,
                          CountryRepository countryRepository) {
        this.factionRepository = factionRepository;
        this.conflictRepository = conflictRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public List<FactionResponseDto> list() {
        return factionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public FactionResponseDto get(Long id) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faction not found: " + id));
        return toDto(faction);
    }

    public FactionResponseDto create(FactionRequestDto dto) {
        Faction faction = new Faction();
        apply(dto, faction);
        return toDto(factionRepository.save(faction));
    }

    public FactionResponseDto update(Long id, FactionRequestDto dto) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faction not found: " + id));
        apply(dto, faction);
        return toDto(faction);
    }

    public void delete(Long id) {
        if (!factionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Faction not found: " + id);
        }
        factionRepository.deleteById(id);
    }

    private void apply(FactionRequestDto dto, Faction faction) {
        Conflict conflict = conflictRepository.findById(dto.conflictId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conflict not found: " + dto.conflictId()));

        faction.setName(dto.name());
        faction.setConflict(conflict);
        faction.setSupportedCountries(resolveCountries(dto.supportedCountryCodes()));
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

    private FactionResponseDto toDto(Faction faction) {
        Set<String> countryCodes = faction.getSupportedCountries().stream()
                .map(Country::getCode)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        return new FactionResponseDto(
                faction.getId(),
                faction.getName(),
                faction.getConflict().getId(),
                faction.getConflict().getName(),
                countryCodes
        );
    }
}
