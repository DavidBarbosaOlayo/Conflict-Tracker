package com.example.conflictTracker.repo;

import com.example.conflictTracker.model.Faction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactionRepository extends JpaRepository<Faction, Long> {
}
