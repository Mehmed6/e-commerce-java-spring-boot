package com.doganmehmet.app.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jwt_token")
@Getter
@Setter
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jwt_token_id")
    private long jwtTokenId;

    @Column(name = "jwt_token", nullable = false, unique = true)
    private String jwtToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token", nullable = false)
    private RefreshToken refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
