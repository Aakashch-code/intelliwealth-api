package com.example.intelliwealth.exception;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;

    public ApiError() {
        this.timestamp = Instant.now();
    }

    public ApiError(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
