package org.example.eventregistration.service;

import org.example.eventregistration.dto.AuthRequest;
import org.example.eventregistration.dto.AuthResponse;
import org.example.eventregistration.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManager authManager, JwtService jwtService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public User register(AuthRequest request) {
        return userService.registerUser(request.getUsername(), request.getPassword(), "USER");
    }

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtService.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
