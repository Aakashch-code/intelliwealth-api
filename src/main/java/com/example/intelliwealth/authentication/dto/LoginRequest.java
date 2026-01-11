package com.example.intelliwealth.authentication.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String login; // username OR email
    private String password;
}
