package com.example.intelliwealth.authentication.repository;


import com.example.intelliwealth.authentication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {

    // Check existence without returning data (faster/safer)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.username = :login OR u.email = :login")
    Optional<Users> findByUsernameOrEmail(@Param("login") String login);

}