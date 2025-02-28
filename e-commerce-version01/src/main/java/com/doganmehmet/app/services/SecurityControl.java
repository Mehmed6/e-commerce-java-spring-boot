package com.doganmehmet.app.services;

import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Roles;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.IUserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityControl {

    private final IUserRepository m_userRepository;
    private final AuthenticationProvider m_authenticationProvider;

    public SecurityControl(IUserRepository userRepository, AuthenticationProvider authenticationProvider)
    {
        m_userRepository = userRepository;
        m_authenticationProvider = authenticationProvider;
    }

    public void checkTokenUserMatch(String username)
    {
        var token = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals(token))
            throw new ApiException(MyError.INVALID_TOKEN_FOR_USER);
    }

    public boolean isAdmin(String username, String password) throws AuthenticationException
    {
        var user = m_userRepository.findByUsername(username);

        try {
            if (user.isPresent() && user.get().getRoles() == Roles.ADMIN) {
                var auth = new UsernamePasswordAuthenticationToken(username, password);
                m_authenticationProvider.authenticate(auth);

                return true;
            }
        }
        catch (Exception e) {
            throw new ApiException(MyError.PASSWORD_INCORRECT);
        }

        return false;
    }

    public User isUser(String username, String password)
    {
        var user = m_userRepository.findByUsername(username);

        if (user.isPresent()) {
            try {
                var auth = new UsernamePasswordAuthenticationToken(username, password);
                m_authenticationProvider.authenticate(auth);
                return user.get();
            }
            catch (Exception e) {
                throw new ApiException(MyError.PASSWORD_INCORRECT);
            }
        }

        throw new ApiException(MyError.USER_NOT_FOUND);
    }
}
