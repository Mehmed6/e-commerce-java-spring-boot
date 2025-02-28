package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByRoles(Roles roles);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
}
