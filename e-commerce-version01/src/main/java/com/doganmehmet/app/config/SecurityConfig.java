package com.doganmehmet.app.config;

import com.doganmehmet.app.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String REGISTER = "/register/**";
    private static final String LOGIN = "/auth/login/**";
    private static final String REFRESH_TOKEN = "/refreshToken/**";
    private static final String ADMIN = "/admin/**";
    private static final String DASHBOARD = "/dashboard";
    private static final String PUBLIC = "/public/**";

    private final AuthenticationProvider m_authenticationProvider;
    private final JWTAuthenticationFilter m_authenticationFilter;
    public SecurityConfig(AuthenticationProvider authenticationProvider, JWTAuthenticationFilter authenticationFilter)
    {
        m_authenticationProvider = authenticationProvider;
        m_authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain m_securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers(REGISTER, LOGIN, REFRESH_TOKEN, DASHBOARD, ADMIN, PUBLIC).permitAll()
                        .requestMatchers("/css/**", "/favicon.ico").permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(m_authenticationProvider)
                .addFilterBefore(m_authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().disable()
                .logout(logout -> logout.logoutUrl("/logout").permitAll());


        return http.build();
    }
}
