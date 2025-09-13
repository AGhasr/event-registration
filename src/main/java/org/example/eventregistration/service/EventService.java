package org.example.eventregistration.service;

import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import org.example.eventregistration.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> !event.getDate().isBefore(LocalDate.now()))
                .toList();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event deletedEvent = eventRepository.findById(eventId).orElseThrow();
        for (User user : deletedEvent.getParticipants()) {
            user.getRegisteredEvents().remove(deletedEvent);
            userRepository.save(user);
        }
        eventRepository.delete(deletedEvent);
    }

    @Transactional
    public void registerUserForEvent(String username, Long eventId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();

        if (user.getRegisteredEvents().contains(event)) {
            throw new IllegalStateException("User already registered!");
        }
        user.getRegisteredEvents().add(event);
        userRepository.save(user);
    }

    @Transactional
    public void unregisterUserFromEvent(String username, Long eventId) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();

        if (!user.getRegisteredEvents().contains(event)) {
            throw new IllegalStateException("User not registered for this event!");
        }
        user.getRegisteredEvents().remove(event);
        userRepository.save(user);
    }

    public List<Event> getUserEvents(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getRegisteredEvents();
    }
}
