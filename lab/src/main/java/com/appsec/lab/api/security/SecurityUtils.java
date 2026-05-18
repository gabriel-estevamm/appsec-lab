package com.appsec.lab.api.security;

import com.appsec.lab.api.model.user.User;
import com.appsec.lab.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
