package com.example.intelliwealth.authentication.infrastrucutre.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secret;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // ========================
    // Signing Key
    // ========================
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ========================
    // Generate Token (with User ID)
    // ========================
    public String generateToken(UUID userId) {

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    // ========================
    // Extract User ID
    // ========================
    public UUID extractUserId(String token) {

        String subject = extractClaim(token, Claims::getSubject);

        try {
            return UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid JWT subject (not UUID)");
        }
    }

    // ========================
    // Validate Token
    // ========================
    public boolean isTokenValid(String token, UUID userId) {

        UUID tokenUserId = extractUserId(token);

        return tokenUserId.equals(userId)
                && !isTokenExpired(token);
    }

    // ========================
    // Generic Claim Reader
    // ========================
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {
        return resolver.apply(extractAllClaims(token));
    }

    // ========================
    // Parse Token
    // ========================
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ========================
    // Expiration Check
    // ========================
    private boolean isTokenExpired(String token) {

        Date expiration =
                extractClaim(token, Claims::getExpiration);

        return expiration.before(new Date());
    }
}
