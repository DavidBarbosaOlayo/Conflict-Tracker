package com.example.conflictTracker.service;

import com.example.conflictTracker.dto.CountryResponseDto;
import com.example.conflictTracker.repo.CountryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryResponseDto> list() {
        return countryRepository.findAll(Sort.by("name")).stream()
                .map(country -> new CountryResponseDto(country.getId(), country.getName(), country.getCode()))
                .toList();
    }
}
