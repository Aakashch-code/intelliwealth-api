package com.example.intelliwealth.authentication.controller;


import com.example.intelliwealth.authentication.dto.AuthResponse;
import com.example.intelliwealth.authentication.dto.LoginRequest;
import com.example.intelliwealth.authentication.dto.RegisterRequest;
import com.example.intelliwealth.authentication.model.Users;
import com.example.intelliwealth.authentication.repository.UserRepository;
import com.example.intelliwealth.authentication.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {


        if (repo.existsByUsername(request.getUsername()) || repo.existsByEmail(request.getEmail())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("User registration failed");
        }

        Users user = new Users(
                null, // UUID generated automatically
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                "USER"
        );

        repo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // CRITICAL: Never say "Wrong Password" vs "User not found"
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // We assume login was username OR email, but token should subject the specific username
        // We need to fetch the actual User object to get the canonical username
        Users user = repo.findByUsernameOrEmail(request.getLogin()).orElseThrow();

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}