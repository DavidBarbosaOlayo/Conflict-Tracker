package com.example.conflictTracker.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @ManyToMany(mappedBy = "countries")
    private Set<Conflict> conflicts = new HashSet<>();

    @ManyToMany(mappedBy = "supportedCountries")
    private Set<Faction> factions = new HashSet<>();


    public Country() {}

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Conflict> getConflicts() {
        return conflicts;
    }

    public void setConflicts(Set<Conflict> conflicts) {
        this.conflicts = conflicts;
    }

    public Set<Faction> getFactions() {
        return factions;
    }

    public void setFactions(Set<Faction> factions) {
        this.factions = factions;
    }
}
