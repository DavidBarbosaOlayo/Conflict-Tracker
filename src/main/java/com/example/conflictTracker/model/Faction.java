package com.example.conflictTracker.model;

import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;

@Entity
public class Faction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "conflict_id")
    private Conflict conflict;

    @ManyToMany
    @JoinTable(name = "faction_country", joinColumns = @JoinColumn(name = "faction_id"), inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private Set<Country> supportedCountries = new HashSet<>();

}
