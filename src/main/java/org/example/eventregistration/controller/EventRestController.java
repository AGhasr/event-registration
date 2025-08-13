package org.example.eventregistration.controller;

import org.example.eventregistration.dto.EventDTO;
import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import org.example.eventregistration.repository.UserRepository;
import org.example.eventregistration.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final JwtService jwtService;

    public EventRestController(EventRepository eventRepo, UserRepository userRepo, JwtService jwtService) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    // GET /api/events
    @GetMapping
    public List<EventDTO> getAllUpcomingEvents() {
        return eventRepo.findAll()
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepo.findByUsername(username).orElseThrow();
        Event event = eventRepo.findById(eventId).orElseThrow();

        if (!user.getRegisteredEvents().contains(event)) {
            user.getRegisteredEvents().add(event);
            userRepo.save(user);
            return ResponseEntity.ok("Registered successfully.");
        } else {
            return ResponseEntity.badRequest().body("Already registered.");
        }
    }

    // /api/events/mine
    @GetMapping("/mine")
    public List<EventDTO> getMyRegisteredEvents() {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepo.findByUsername(username).orElseThrow();

        return user.getRegisteredEvents()
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepo.findByUsername(username).orElseThrow();
        Event event = eventRepo.findById(eventId).orElseThrow();

        if (user.getRegisteredEvents().contains(event)) {
            user.getRegisteredEvents().remove(event);
            userRepo.save(user);
            return ResponseEntity.ok("Unregistered successfully.");
        } else {
            return ResponseEntity.badRequest().body("You were not registered for this event.");
        }
    }

}
