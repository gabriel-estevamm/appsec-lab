package com.appsec.lab.api.controller;

import com.appsec.lab.api.dto.AuthRequest;
import com.appsec.lab.api.dto.AuthResponse;
import com.appsec.lab.api.dto.UserRequest;
import com.appsec.lab.api.dto.UserResponse;
import com.appsec.lab.api.service.UserService;
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

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public UserResponse register(@Valid @RequestBody UserRequest request) {
        return userService.registerClient(request);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return userService.login(request);
    }

    @PutMapping("/{id}/client")
    @PreAuthorize("hasRole('CLIENT')")
    public UserResponse updateClient(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return userService.updateClient(id, request);
    }

    @DeleteMapping("/{id}/client")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        userService.deleteClient(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<UserResponse> listAll() {
        return userService.listAll();
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('MANAGER')")
    public UserResponse getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PutMapping("/{id}/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public UserResponse updateClientName(@PathVariable Long id, @RequestParam String newName) {
        return userService.updateClientName(id, newName);
    }

    @PutMapping("/{id}/admin/password")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updatePassword(@PathVariable Long id, @RequestParam String newPassword) {
        return userService.updatePassword(id, newPassword);
    }
}
