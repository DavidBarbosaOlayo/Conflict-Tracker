package com.example.conflictTracker.repo;

import com.example.conflictTracker.model.Conflict;
import com.example.conflictTracker.model.ConflictStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConflictRepository extends JpaRepository<Conflict, Long> {
    List<Conflict> findByStatus(ConflictStatus status);

    List<Conflict> findDistinctByCountries_CodeIgnoreCase(String code);
}
