package com.example.conflictTracker.repo;

import com.example.conflictTracker.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
