package org.example.eventregistration.controller;


import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import org.example.eventregistration.repository.UserRepository;
import org.example.eventregistration.service.EventService;
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

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    /**
     * Handles registration of the authenticated user to a specific event.
     * Prevents duplicate registrations.
     */
    @PostMapping("/registerEvent/{id}")
    public String registerEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            eventService.registerUserForEvent(userDetails.getUsername(), id);
            redirectAttributes.addFlashAttribute("message", "Successfully registered!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/";
    }

    /**
     * Cancels a user's registration for a specific event.
     */
    @PostMapping("/cancelRegistration/{id}")
    public String cancelRegistration(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails,
                                     RedirectAttributes redirectAttributes) {
        try {
            eventService.unregisterUserFromEvent(userDetails.getUsername(), id);
            redirectAttributes.addFlashAttribute("message", "Unregistered successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/";
    }


    /**
     * Displays the list of events that the authenticated user is registered for.
     */
    @GetMapping("/my-events")
    public String myEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("myEvents", eventService.getUserEvents(userDetails.getUsername()));
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
        eventService.createEvent(event);
        return "redirect:/";
    }

    /**
     * Deletes an event (admin only) and removes it from all users' registrations.
     */
    @PostMapping("/admin/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId) {

        eventService.deleteEvent(eventId);

        return "redirect:/";
    }

    /**
     * Display the list of upcoming events on the homepage.
     * If a user is logged in, their registered events are highlighted.
     */
    @GetMapping("/")
    public String allEvents(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("events", eventService.getUpcomingEvents());

        if (userDetails != null) {
            model.addAttribute("registeredEvents", eventService.getUserEvents(userDetails.getUsername()));
        } else {
            model.addAttribute("registeredEvents", List.of());
        }

        return "events";
    }


}
