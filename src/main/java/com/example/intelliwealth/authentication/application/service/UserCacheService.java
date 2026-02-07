package com.example.intelliwealth.authentication.application.service;

import com.example.intelliwealth.authentication.application.dto.CachedUser;
import com.example.intelliwealth.authentication.domain.Users;
import com.example.intelliwealth.authentication.infrastrucutre.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final UserRepository repo;

    @Cacheable(value = "user:v1", key = "#id")
    public CachedUser getUserById(UUID id) {

        Users user = repo.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + id)
                );

        return new CachedUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
