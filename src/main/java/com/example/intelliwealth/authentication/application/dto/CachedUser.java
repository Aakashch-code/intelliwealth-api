package com.example.intelliwealth.authentication.application.dto;

import java.util.UUID;

public record CachedUser(
        UUID id,
        String username,
        String email,
        String role
) {}
