package com.app.authservice.service;

import com.app.authservice.auth.dto.AuthResponse;
import com.app.authservice.auth.dto.LoginRequest;
import com.app.authservice.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}