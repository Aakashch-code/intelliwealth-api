package com.example.intelliwealth.authentication.application;

import com.example.intelliwealth.authentication.domain.Users;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public abstract class SecuredService {

    protected UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Users user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        return user.getId();
    }
}
