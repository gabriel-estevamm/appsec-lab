package com.appsec.lab.api.config;

import com.appsec.lab.api.model.user.User;
import com.appsec.lab.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(User.builder()
                        .username("admin")
                        .fullName("admin")
                        .password(encoder.encode("admin123"))
                        .role("ROLE_ADMIN")
                        .build());
            }
            if (userRepository.findByUsername("agent").isEmpty()) {
                userRepository.save(User.builder()
                        .username("agent")
                        .fullName("John Doe")
                        .password(encoder.encode("agent23"))
                        .role("ROLE_AGENT")
                        .build());
            }
        };
    }
}
