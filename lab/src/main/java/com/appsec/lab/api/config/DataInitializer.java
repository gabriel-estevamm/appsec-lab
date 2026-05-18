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
                        .password(encoder.encode("admin"))
                        .role("ADMIN")
                        .active(true)
                        .build());
            }
            if (userRepository.findByUsername("john").isEmpty()) {
                userRepository.save(User.builder()
                        .username("john")
                        .fullName("John Doe")
                        .password(encoder.encode("user123"))
                        .role("USER")
                        .active(true)
                        .build());
            }
        };
    }
}
