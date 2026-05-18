package com.appsec.lab.api.service;

import com.appsec.lab.api.dto.*;
import com.appsec.lab.api.exception.HttpResponseException;
import com.appsec.lab.api.model.user.User;
import com.appsec.lab.api.repository.UserRepository;
import com.appsec.lab.api.security.JwtService;
import com.appsec.lab.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final SecurityUtils securityUtils;

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new HttpResponseException("Username already exists: " + request.username(), HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(request.username())
                .fullName(request.fullName())
                .password(encoder.encode(request.password()))
                .role(request.role())
                .active(true)
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = findUser(id);
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser == null) {
            throw new HttpResponseException("No authenticated user found", HttpStatus.UNAUTHORIZED);
        }

        switch (currentUser.getRole()) {
            case "USER" -> {
                if (!user.getUsername().equals(currentUser.getUsername())) {
                    throw new HttpResponseException("Users can only update their own profile", HttpStatus.FORBIDDEN);
                }
                updateAsUser(user, request);
            }
            case "ADMIN" -> updateAsAdmin(user, currentUser, request);
            default -> throw new HttpResponseException("Invalid role", HttpStatus.FORBIDDEN);
        }

        return toResponse(userRepository.save(user));
    }

    private void updateAsUser(User user, UserRequest request) {
        user.setFullName(request.fullName());
        user.setPassword(encoder.encode(request.password()));
    }

    private void updateAsAdmin(User user, User currentUser, UserRequest request) {
        if ("USER".equals(user.getRole())) {
            user.setFullName(request.fullName());
            user.setPassword(encoder.encode("user123")); // reset
            user.setRole(request.role());
        } else if (user.getUsername().equals(currentUser.getUsername())) {
            user.setFullName(request.fullName());
            user.setPassword(encoder.encode(request.password()));
        }
    }

    public void deactivateUser(Long id) {
        User user = findUser(id);
        if (!"USER".equals(user.getRole())) {
            throw new HttpResponseException("Only USER accounts can be deactivated", HttpStatus.FORBIDDEN);
        }
        user.setActive(false);
        userRepository.save(user);
    }

    public List<UserResponse> listAll() {
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser == null) {
            throw new HttpResponseException("No authenticated user found", HttpStatus.UNAUTHORIZED);
        }

        List<User> users = "ADMIN".equals(currentUser.getRole())
                ? userRepository.findAll()
                : userRepository.findByRoleAndActiveTrue("USER");

        return users.stream().map(this::toResponse).toList();
    }

    public UserResponse getByUsername(String username) {
        User currentUser = securityUtils.getCurrentUser();

        if (currentUser == null) {
            throw new HttpResponseException("No authenticated user found", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new HttpResponseException("User not found: " + username, HttpStatus.NOT_FOUND));

        if ("USER".equals(currentUser.getRole()) && !"USER".equals(user.getRole())) {
            throw new HttpResponseException("Users can only query other active USER accounts", HttpStatus.FORBIDDEN);
        }

        return toResponse(user);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsernameAndActiveTrue(request.username())
                .orElseThrow(() -> new HttpResponseException("User not found or inactive", HttpStatus.NOT_FOUND));

        String token = jwtService.generateToken(user.getUsername(), "ROLE_" + user.getRole());
        return new AuthResponse(token);
    }

    private User findUser(Long id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new HttpResponseException("User not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getActive()
        );
    }
}
