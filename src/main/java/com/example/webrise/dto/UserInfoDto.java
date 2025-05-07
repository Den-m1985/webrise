package com.example.webrise.dto;

import com.example.webrise.model.enums.Gender;

public record UserInfoDto(
        String firstName,
        String middleName,
        String lastName,
        String email,
        Gender gender,
        String role
) {
}
