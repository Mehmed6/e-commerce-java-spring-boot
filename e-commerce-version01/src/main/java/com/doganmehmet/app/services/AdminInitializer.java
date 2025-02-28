package com.doganmehmet.app.services;

import com.doganmehmet.app.entity.Account;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Roles;
import com.doganmehmet.app.jwt.JWTTransactions;
import com.doganmehmet.app.repositories.IAccountRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final IUserRepository m_userRepository;
    private final BCryptPasswordEncoder m_bCryptPasswordEncoder;
    private final JWTTransactions m_jwtTransactions;
    private final JwtTokenService m_jwtTokenService;
    private final RefreshTokenService m_refreshTokenService;
    private final IAccountRepository m_accountRepository;

    public AdminInitializer(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTTransactions jwtTransactions, JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService, IAccountRepository accountRepository)
    {
        m_userRepository = userRepository;
        m_bCryptPasswordEncoder = bCryptPasswordEncoder;
        m_jwtTransactions = jwtTransactions;
        m_jwtTokenService = jwtTokenService;
        m_refreshTokenService = refreshTokenService;
        m_accountRepository = accountRepository;
    }

    @Override
    public void run(String... args)
    {
        if (!m_userRepository.existsByRoles(Roles.ADMIN)) {
            var admin = new User();
            admin.setUsername("admin");
            admin.setPassword(m_bCryptPasswordEncoder.encode("admin"));
            admin.setEmail("admin@gmail.com");
            admin.setCreated_at(LocalDateTime.now());
            admin.setRoles(Roles.ADMIN);

            m_userRepository.save(admin);

            // daha sonra ele alinacak
//            var jwtToken = m_jwtTransactions.generateToken(admin);
//            var refreshToken = m_jwtTransactions.createRefreshToken(admin);
//
//
//            m_refreshTokenService.save(refreshToken);
//            m_jwtTokenService.save(jwtToken, refreshToken, admin);

            System.out.println("Admin kullanıcı oluşturuldu: admin@example.com / admin123");

        }
    }
}
