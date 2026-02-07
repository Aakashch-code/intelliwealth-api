package com.example.intelliwealth.authentication.application.service;

import com.example.intelliwealth.authentication.application.dto.CachedUser;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        var user = userRepository.findByUsernameOrEmail(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + login
                        )
                );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // IMPORTANT
                .roles(user.getRole())
                .build();
    }
}
