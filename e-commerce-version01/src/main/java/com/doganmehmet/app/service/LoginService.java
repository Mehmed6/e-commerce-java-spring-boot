package com.doganmehmet.app.service;

import com.doganmehmet.app.entity.JwtToken;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.request.LoginRequest;
import com.doganmehmet.app.jwt.JWTTransactions;
import com.doganmehmet.app.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final IUserRepository m_userRepository;
    private final AuthenticationProvider m_authenticationProvider;
    private final JWTTransactions m_jwtTransactions;
    private final RefreshTokenService m_refreshTokenService;
    private final JwtTokenService m_jwtTokenService;

    public LoginService(IUserRepository userRepository, AuthenticationProvider authenticationProvider, JWTTransactions jwtTransactions, RefreshTokenService refreshTokenService, JwtTokenService jwtTokenService)
    {
        m_userRepository = userRepository;
        m_authenticationProvider = authenticationProvider;
        m_jwtTransactions = jwtTransactions;
        m_refreshTokenService = refreshTokenService;
        m_jwtTokenService = jwtTokenService;
    }

    @Transactional
    public JwtToken login(LoginRequest loginRequest)
    {
        var user = m_userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        try {
            var auth = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            m_authenticationProvider.authenticate(auth);
        } catch (Exception ignore) {
            throw new ApiException(MyError.PASSWORD_INCORRECT);
        }

        var token = m_jwtTokenService.findJwtTokenByUserId(user.getUserId());

        if (token != null) {
            if (!m_jwtTransactions.isTokenExpired(token.getJwtToken()))
                return token;

            m_jwtTokenService.deleteAllByUser(user);
            m_refreshTokenService.deleteAllByUser(user);
        }

        var accessToken = m_jwtTransactions.generateToken(user);
        var refreshToken = m_jwtTransactions.createRefreshToken(user);

        m_refreshTokenService.save(refreshToken);
        return m_jwtTokenService.save(accessToken, refreshToken, user);
    }
}
