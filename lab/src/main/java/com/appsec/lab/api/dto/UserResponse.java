package com.appsec.lab.api.dto;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        String profession,
        String role
) {}
