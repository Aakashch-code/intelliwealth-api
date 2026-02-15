package com.example.intelliwealth.fynix.application.dto;

import java.util.List;

public record GeminiRequest(List<Content> contents) {

    public static GeminiRequest fromText(String text) {

        Part part = new Part(text);
        Content content = new Content(List.of(part));

        return new GeminiRequest(List.of(content));
    }

    public record Content(List<Part> parts) {}

    public record Part(String text) {}
}
