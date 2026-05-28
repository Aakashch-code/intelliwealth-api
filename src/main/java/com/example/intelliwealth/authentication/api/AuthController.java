package com.example.intelliwealth.authentication.api;

import com.example.intelliwealth.authentication.application.dto.AuthResponse;
import com.example.intelliwealth.authentication.application.dto.LoginRequest;
import com.example.intelliwealth.authentication.application.dto.RegisterRequest;
import com.example.intelliwealth.authentication.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}