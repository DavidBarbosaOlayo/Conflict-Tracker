package com.example.conflictTracker.controller;

import com.example.conflictTracker.dto.ConflictResponseDto;
import com.example.conflictTracker.dto.CountryResponseDto;
import com.example.conflictTracker.service.ConflictService;
import com.example.conflictTracker.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;
    private final ConflictService conflictService;

    public CountryController(CountryService countryService, ConflictService conflictService) {
        this.countryService = countryService;
        this.conflictService = conflictService;
    }

    @GetMapping
    public List<CountryResponseDto> list() {
        return countryService.list();
    }

    @GetMapping("/{code}/conflicts")
    public List<ConflictResponseDto> conflictsByCountry(@PathVariable String code) {
        return conflictService.listByCountryCode(code);
    }
}
