package com.example.webrise.dto;

public record AuthResponse(
        Integer userId,
        String name,
        String email
) {
}
