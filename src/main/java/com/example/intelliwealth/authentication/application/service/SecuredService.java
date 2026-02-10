package com.example.intelliwealth.authentication.application.service;

import com.example.intelliwealth.authentication.application.dto.CachedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public abstract class SecuredService {

    public UUID currentUserId() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !(auth.getPrincipal() instanceof CachedUser user)) {

            throw new AccessDeniedException("Unauthenticated");
        }

        return user.id();
    }

    public String cacheKey() {
        return currentUserId().toString();
    }

}
