package com.doganmehmet.app.service;

import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Roles;
import com.doganmehmet.app.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final IUserRepository m_userRepository;
    private final BCryptPasswordEncoder m_bCryptPasswordEncoder;

    public AdminInitializer(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        m_userRepository = userRepository;
        m_bCryptPasswordEncoder = bCryptPasswordEncoder;
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

            System.out.println("Admin has been created: admin@example.com / admin");

        }
    }
}
