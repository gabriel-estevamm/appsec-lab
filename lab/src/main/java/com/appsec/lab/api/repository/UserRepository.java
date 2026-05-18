package com.appsec.lab.api.repository;

import com.appsec.lab.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndActiveTrue(String username);
    Optional<User> findByIdAndActiveTrue(Long id);
    List<User> findByRoleAndActiveTrue(String role);
}