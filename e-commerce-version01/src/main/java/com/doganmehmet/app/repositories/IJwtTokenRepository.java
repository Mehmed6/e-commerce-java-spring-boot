package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.JwtToken;
import com.doganmehmet.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findJwtTokenByUser(User user);

    @Modifying
    @Query("DELETE FROM JwtToken j WHERE j.user = :user")
    void deleteJwtTokenByUser(User user);


}
