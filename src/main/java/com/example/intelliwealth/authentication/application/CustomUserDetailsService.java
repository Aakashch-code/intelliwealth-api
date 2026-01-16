package com.example.intelliwealth.authentication.application;

import com.example.intelliwealth.authentication.domain.Users;
import com.example.intelliwealth.authentication.infrastrucutre.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Correctly maps your Entity to Spring Security's User
        Users user = repo.findByUsernameOrEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        // Don't say "User not found" - say "Invalid credentials" to prevent enumeration

        return User.builder()
                .username(user.getUsername()) // Always use username as principal
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}