package com.example.intelliwealth.authentication.api.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String login; // username OR email
    private String password;
}
