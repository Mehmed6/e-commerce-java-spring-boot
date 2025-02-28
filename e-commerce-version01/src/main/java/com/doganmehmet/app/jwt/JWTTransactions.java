package com.doganmehmet.app.jwt;

import com.doganmehmet.app.entity.RefreshToken;
import com.doganmehmet.app.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JWTTransactions {

    private static final String SECRET_KEY = "6cdb36ba0549224d0e8f70cb4e48d343e8b6427d52e9715d20072f62de1e4a897e7e3b28a3dda1a324110f3e754fbf8e4b356b0645faa94494291b411d7acc12";
    private Key getKey()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    private Claims getClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails)
    {
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshToken(User user)
    {
        var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiresDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));

        return refreshToken;
    }

    public List<String> getRolesFromToken(String token) {
        var claims = getClaims(token);
        return claims.get("roles", List.class);
    }

    public String getUserNameByToken(String token)
    {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token)
    {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception ignored) {
            return true;
        }
    }
}
