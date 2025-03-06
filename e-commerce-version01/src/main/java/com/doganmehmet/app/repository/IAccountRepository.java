package com.doganmehmet.app.repository;

import com.doganmehmet.app.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(String accountNo);

    Optional<Account> findByUsername(String username);

    @Transactional
    void deleteAccountByUsername(String username);
}
