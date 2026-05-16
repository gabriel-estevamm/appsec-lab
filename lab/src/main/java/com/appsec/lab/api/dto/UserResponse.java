package com.appsec.lab.api.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {}
