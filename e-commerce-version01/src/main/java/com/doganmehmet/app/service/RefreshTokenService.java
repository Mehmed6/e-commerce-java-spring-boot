package com.doganmehmet.app.service;

import com.doganmehmet.app.entity.RefreshToken;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.jwt.JWTTransactions;
import com.doganmehmet.app.repository.IRefreshTokenRepository;
import com.doganmehmet.app.response.LoginResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenService {

    private final IRefreshTokenRepository m_refreshTokenRepository;
    private final JwtTokenService m_jwtTokenService;
    private final JWTTransactions m_jwtTransactions;

    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository, JwtTokenService jwtTokenService, JWTTransactions jwtTransactions)
    {
        m_refreshTokenRepository = refreshTokenRepository;
        m_jwtTokenService = jwtTokenService;
        m_jwtTransactions = jwtTransactions;
    }

    public LoginResponse refreshToken(String refreshToken)
    {
        var token = m_refreshTokenRepository.findByRefreshToken(refreshToken);

        if (token.isEmpty())
            throw new ApiException(MyError.REFRESH_TOKEN_NOT_FOUND);

        if (token.get().getExpiresDate().before(new Date()))
            throw new ApiException(MyError.REFRESH_TOKEN_EXPIRED);

        var user = token.get().getUser();
        m_jwtTokenService.deleteAllByUser(user);
        m_refreshTokenRepository.deleteAllByUser(user);


        var newRefreshToken = m_refreshTokenRepository.save(m_jwtTransactions.createRefreshToken(user));
        var accessToken = m_jwtTokenService.save(m_jwtTransactions.generateToken(user), newRefreshToken, user);

        return new LoginResponse(accessToken.getJwtToken(), newRefreshToken.getRefreshToken());

    }

    public void save(RefreshToken refreshToken)
    {
        m_refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteAllByUser(User user)
    {
        m_refreshTokenRepository.deleteAllByUser(user);
    }
}
