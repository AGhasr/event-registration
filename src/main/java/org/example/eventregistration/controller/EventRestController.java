package org.example.eventregistration.controller;

import org.example.eventregistration.dto.EventDTO;
import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import org.example.eventregistration.repository.UserRepository;
import org.example.eventregistration.service.EventService;
import org.example.eventregistration.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventService eventService;

    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    // GET /api/events
    @GetMapping
    public List<EventDTO> getAllUpcomingEvents() {
        return eventService.getUpcomingEvents()
                .stream()
                .map(e -> new EventDTO(
                        e.getId(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getDate()
                ))
                .toList();
    }

    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> registerForEvent(@PathVariable Long eventId) {
        // Get currently authenticated username from Spring context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            eventService.registerUserForEvent(username, eventId);
            return ResponseEntity.ok("Registered successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // /api/events/mine
    @GetMapping("/mine")
    public List<EventDTO> getMyRegisteredEvents() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return eventService.getUserEvents(username)
                .stream()
                .map(e -> new EventDTO(
                        e.getId(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getDate()
                ))
                .toList();
    }

    //  /api/events/unregister/3
    @PostMapping("/unregister/{eventId}")
    public ResponseEntity<String> unregisterFromEvent(@PathVariable Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            eventService.unregisterUserFromEvent(username, eventId);
            return ResponseEntity.ok("Unregistered successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
