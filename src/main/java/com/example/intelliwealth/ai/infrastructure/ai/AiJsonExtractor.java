package com.example.intelliwealth.ai.infrastructure.ai;

    public class AiJsonExtractor {

        public static String extractJson(String text) {

            if (text == null) throw new IllegalArgumentException("Empty AI response");

            text = text.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .replaceAll("%", "")  // remove % symbols
                    .trim();

            int start = text.indexOf("{");
            int end = text.lastIndexOf("}");

            if (start < 0 || end < 0 || start >= end) {
                throw new IllegalArgumentException("No valid JSON found");
            }

            return text.substring(start, end + 1);
        }
    }

