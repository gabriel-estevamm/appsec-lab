package com.appsec.lab.api.repository;

import com.appsec.lab.api.model.account.Account;
import com.appsec.lab.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByOwner(User owner);
}
