package com.appsec.lab.api.controller;

import com.appsec.lab.api.dto.*;
import com.appsec.lab.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user. Restricted to ADMIN")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse register(@Valid @RequestBody UserRequest request) {
        return userService.registerUser(request);
    }

    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return userService.login(request);
    }

    @Operation(summary = "Update user", description = "USER updates own profile. ADMIN updates own or USER accounts.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Deactivate user", description = "ADMIN can deactivate USER accounts")
    @ApiResponse(responseCode = "204", description = "User deactivated successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    @Operation(summary = "List users", description = "USER lists active USER accounts. ADMIN lists all users.")
    @ApiResponse(responseCode = "200", description = "List of users returned")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<UserResponse> listAll() {
        return userService.listAll();
    }

    @Operation(summary = "Get user by username", description = "USER queries active USER accounts. ADMIN queries any user.")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
}
