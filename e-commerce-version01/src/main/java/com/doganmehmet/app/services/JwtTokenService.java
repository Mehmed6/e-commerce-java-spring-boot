package com.doganmehmet.app.services;

import com.doganmehmet.app.entity.JwtToken;
import com.doganmehmet.app.entity.RefreshToken;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.IJwtTokenRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private final IJwtTokenRepository m_jwtTokenRepository;
    private final IUserRepository m_userRepository;

    public JwtTokenService(IJwtTokenRepository jwtTokenRepository, IUserRepository userRepository)
    {
        m_jwtTokenRepository = jwtTokenRepository;
        m_userRepository = userRepository;
    }

    public JwtToken save(String jwtToken, RefreshToken refreshToken, User user)
    {
        var token = new JwtToken();
        token.setJwtToken(jwtToken);
        token.setRefreshToken(refreshToken);
        token.setUser(user);

        return m_jwtTokenRepository.save(token);
    }

    public JwtToken findJwtTokenByUserId(long userId)
    {
        var user = m_userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        return m_jwtTokenRepository.findJwtTokenByUser(user)
                .orElse(null);
    }

    @Transactional
    public void deleteAllByUser(User user)
    {
        m_jwtTokenRepository.deleteJwtTokenByUser(user);
    }
}
