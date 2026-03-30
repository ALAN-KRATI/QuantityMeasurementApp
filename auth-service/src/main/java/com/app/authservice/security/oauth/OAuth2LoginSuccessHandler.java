package com.app.authservice.security.oauth;

import com.app.authservice.security.jwt.JwtService;
import com.app.authservice.user.entity.Role;
import com.app.authservice.user.entity.User;
import com.app.authservice.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Email not found from OAuth2 provider"
            );
            return;
        }

        User appUser = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(
                            name == null ? "Google User" : name,
                            email,
                            null,
                            Role.USER,
                            "GOOGLE"
                    );

                    return userRepository.save(newUser);
                });

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        appUser.getEmail(),
                        "",
                        Collections.singletonList(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + appUser.getRole().name()
                                )
                        )
                );

        String token = jwtService.generateToken(userDetails);

        String redirectUrl =
                "http://localhost:5173/oauth2-success?token=" +
                        URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}