package com.example.authjwt.service;

import com.example.authjwt.entity.User;
import com.example.authjwt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(String username, String rawPassword) {
        if (repo.existsByUsername(username)) {
            throw new RuntimeException("Username already taken");
        }
        String hashed = encoder.encode(rawPassword);
        User u = new User(username, hashed);
        return repo.save(u);
    }
}
