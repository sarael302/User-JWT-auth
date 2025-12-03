package com.example.authjwt.controller;

import com.example.authjwt.dto.*;
import com.example.authjwt.entity.User;
import com.example.authjwt.repository.UserRepository;
import com.example.authjwt.security.JwtService;
import com.example.authjwt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserRepository userRepository,
                          PasswordEncoder encoder, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        userService.register(req.getUsername(), req.getPassword());
        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        return userRepository.findByUsername(req.getUsername())
                .map(u -> {
                    if (encoder.matches(req.getPassword(), u.getPassword())) {
                        String token = jwtService.generateToken(u.getUsername());
                        return ResponseEntity.ok(new AuthResponse(token));
                    } else {
                        return ResponseEntity.status(401).body("Invalid credentials");
                    }
                }).orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}
