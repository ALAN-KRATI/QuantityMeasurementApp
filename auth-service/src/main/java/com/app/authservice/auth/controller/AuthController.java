package com.app.authservice.auth.controller;

import com.app.authservice.auth.dto.AuthResponse;
import com.app.authservice.auth.dto.LoginRequest;
import com.app.authservice.auth.dto.RegisterRequest;
import com.app.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
    origins = {"${CORS_ALLOWED_ORIGINS:https://quantity-measurement-app-frontend-1.vercel.app}"},
    allowedHeaders = "*",
    allowCredentials = "true"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}