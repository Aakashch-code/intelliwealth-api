package com.example.intelliwealth.authentication.application.service;

import com.example.intelliwealth.authentication.application.dto.AuthResponse;
import com.example.intelliwealth.authentication.application.dto.LoginRequest;
import com.example.intelliwealth.authentication.application.dto.RegisterRequest;
import com.example.intelliwealth.authentication.domain.Users;
import com.example.intelliwealth.authentication.infrastrucutre.persistence.UserRepository;
import com.example.intelliwealth.authentication.infrastrucutre.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public void registerUser(RegisterRequest request) {
        if (repo.existsByUsername(request.getUsername())
                || repo.existsByEmail(request.getEmail())) {
            // Throwing an exception allows the controller to map it to a 409 Conflict
            throw new IllegalStateException("User registration failed");
        }

        Users user = new Users(
                null,
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                "USER"
        );

        repo.save(user);
    }

    public AuthResponse loginUser(LoginRequest request) {
        // 1. Authenticate credentials
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        // 2. Fetch user
        Users user = repo.findByUsernameOrEmail(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // 3. Generate token
        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponse(token);
    }
}