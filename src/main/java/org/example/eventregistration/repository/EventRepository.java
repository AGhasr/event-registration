package org.example.eventregistration.repository;

import org.example.eventregistration.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
