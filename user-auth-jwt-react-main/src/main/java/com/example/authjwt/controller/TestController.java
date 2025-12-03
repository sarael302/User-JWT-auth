package com.example.authjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/secure")
    public String secure() {
        return "Contenu protégé - accès réussi";
    }
}
