package com.doganmehmet.app.config;

import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig{
    private final IUserRepository m_userRepository;

    public AppConfig(IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return username -> m_userRepository.findByUsername(username).
                orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        var autProvider = new DaoAuthenticationProvider();
        autProvider.setUserDetailsService(userDetailsService());
        autProvider.setPasswordEncoder(passwordEncoder());

        return autProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
