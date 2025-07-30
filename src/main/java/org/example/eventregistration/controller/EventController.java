package org.example.eventregistration.controller;


import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import org.example.eventregistration.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
public class EventController {

    private final UserRepository userRepo;
    private final EventRepository eventRepository;

    public EventController(UserRepository userRepo, EventRepository eventRepository) {
        this.userRepo = userRepo;
        this.eventRepository = eventRepository;
    }


    /**
     * Handles registration of the authenticated user to a specific event.
     * Prevents duplicate registrations.
     */
    @PostMapping("/registerEvent/{id}")
    public String registerEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Event event = eventRepository.findById(id).orElseThrow();

        if (!user.getRegisteredEvents().contains(event)) {
            user.getRegisteredEvents().add(event);
            userRepo.save(user);
            redirectAttributes.addFlashAttribute("message", "Successfully registered!");

        }
        else{
            redirectAttributes.addFlashAttribute("message", "Already registered!");
        }

        return "redirect:/";
    }

    /**
     * Cancels a user's registration for a specific event.
     */
    @PostMapping("/cancelRegistration/{eventId}")
    public String cancelRegistration(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();

        user.getRegisteredEvents().remove(event);
        userRepo.save(user); // Save the updated user with one less registered event

        return "redirect:/my-events?message=Unregistered+successfully";
    }


    /**
     * Displays the list of events that the authenticated user is registered for.
     */
    @GetMapping("/my-events")
    public String myEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("myEvents", user.getRegisteredEvents());
        return "my-events";
    }


    /**
     * Displays the form for creating a new event (admin only).
     */
    @GetMapping("/admin/new")
    public String createForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    /**
     * Handles the submission of a new event created by an admin.
     */
    @PostMapping("/admin/new")
    public String createEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/";
    }

    /**
     * Deletes an event (admin only) and removes it from all users' registrations.
     */
    @PostMapping("/admin/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow();

        // Loop through participants and remove this event from each user's registered events
        for (User user : event.getParticipants()) {
            user.getRegisteredEvents().remove(event);
            userRepo.save(user);
        }

        // Now it's safe to delete the event
        eventRepository.delete(event);

        return "redirect:/";
    }

    /**
     * Display the list of upcoming events on the homepage.
     * If a user is logged in, their registered events are highlighted.
     */
    @GetMapping("/")
    public String allEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Event> all = eventRepository.findAll();
        List<Event> upcoming = all.stream()
                .filter(e -> e.getDate().isAfter(LocalDate.now()))
                .toList();

        model.addAttribute("events", upcoming);

        if (userDetails != null) {
            User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
            model.addAttribute("registeredEvents", user.getRegisteredEvents());
        } else {
            model.addAttribute("registeredEvents", List.of());
        }

        return "events";
    }


}
