package com.appsec.lab.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Full name is mandatory")
        String fullName,
        @NotBlank(message = "Profession is mandatory")
        String profession,
        @NotBlank(message = "Password is mandatory")
        String password
) {}
