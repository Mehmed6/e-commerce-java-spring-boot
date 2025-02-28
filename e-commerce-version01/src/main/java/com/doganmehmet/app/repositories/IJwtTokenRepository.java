package com.doganmehmet.app.repositories;

import com.doganmehmet.app.entity.JwtToken;
import com.doganmehmet.app.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findJwtTokenByUser(User user);


//  boolean existsJwtTokenByUserId(Long userId);

    @Query("SELECT COUNT(j) > 0 FROM JwtToken j WHERE j.jwtToken = :token")
    boolean existsByJwt_token(@Param("token") String token);


    @Modifying
    @Query("DELETE FROM JwtToken j WHERE j.user = :user")
    void deleteJwtTokenByUser(User user);


}
