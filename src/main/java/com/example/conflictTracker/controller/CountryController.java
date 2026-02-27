package com.example.conflictTracker.controller;

import com.example.conflictTracker.dto.ConflictResponseDto;
import com.example.conflictTracker.service.ConflictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final ConflictService conflictService;

    public CountryController(ConflictService conflictService) {
        this.conflictService = conflictService;
    }

    @GetMapping("/{code}/conflicts")
    public List<ConflictResponseDto> conflictsByCountry(@PathVariable String code) {
        return conflictService.listByCountryCode(code);
    }
}
