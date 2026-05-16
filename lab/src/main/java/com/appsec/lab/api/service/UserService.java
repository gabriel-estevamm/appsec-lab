package com.appsec.lab.api.service;

import com.appsec.lab.api.dto.AuthRequest;
import com.appsec.lab.api.dto.AuthResponse;
import com.appsec.lab.api.dto.UserRequest;
import com.appsec.lab.api.dto.UserResponse;
import com.appsec.lab.api.exception.HttpResponseException;
import com.appsec.lab.api.model.user.User;
import com.appsec.lab.api.repository.UserRepository;
import com.appsec.lab.api.security.JwtService;
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

    public UserResponse registerClient(UserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new HttpResponseException("Username already exists: " + request.username(), HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .role("ROLE_CLIENT")
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse updateClient(Long id, UserRequest request) {
        User user = findUser(id);
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        return toResponse(userRepository.save(user));
    }

    public void deleteClient(Long id) {
        if (!userRepository.existsById(id)) {
            throw new HttpResponseException("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpResponseException("User not found: " + username, HttpStatus.NOT_FOUND));
        return toResponse(user);
    }

    public UserResponse updateClientName(Long id, String newName) {
        User user = findUser(id);
        user.setUsername(newName);
        return toResponse(userRepository.save(user));
    }

    public UserResponse updatePassword(Long id, String newPassword) {
        User user = findUser(id);
        user.setPassword(encoder.encode(newPassword));
        return toResponse(userRepository.save(user));
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new HttpResponseException("User not found", HttpStatus.NOT_FOUND));

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HttpResponseException("User not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
