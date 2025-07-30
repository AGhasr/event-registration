package org.example.eventregistration.config;

import org.example.eventregistration.model.Event;
import org.example.eventregistration.model.User;
import org.example.eventregistration.repository.EventRepository;
import jakarta.annotation.PostConstruct;
import org.example.eventregistration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Loads initial data into the database after application startup.
 * Creates a default admin user and some sample events.
 */
@Component
public class DataLoader {

    private final EventRepository repo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(EventRepository repo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void loadData() {
        // Create admin if not exists
        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ADMIN");
            userRepo.save(admin);
        }


        repo.save(new Event("Spring Boot Workshop", "Learn Spring Boot in 1 day", LocalDate.now().plusDays(5)));
        repo.save(new Event("Docker Basics", "Get started with containers", LocalDate.now().plusDays(10)));
        System.out.println("Default admin and events created.");

    }
}
