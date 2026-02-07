package com.example.intelliwealth.authentication.infrastrucutre.persistence;


import com.example.intelliwealth.authentication.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.username = :login OR u.email = :login")
    Optional<Users> findByUsernameOrEmail(@Param("login") String login);

}