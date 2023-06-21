package com.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.domain.services.AuthService;
import com.domain.models.entities.Pengguna;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Pengguna pengguna) {
        String username = pengguna.getUsername();
        String password = pengguna.getPassword();

        // Perform authentication using authService
        String token = authService.authenticate(username, password);

        return token;
    }
}
