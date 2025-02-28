package com.doganmehmet.app.services;

import com.doganmehmet.app.dto.user.UserDTO;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Roles;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.request.RegisterRequest;
import com.doganmehmet.app.mapper.IMapper;
import com.doganmehmet.app.repositories.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterService {

    private final IUserRepository m_userRepository;
    private final BCryptPasswordEncoder m_bCryptPasswordEncoder;
    private final IMapper m_mapper;

    public RegisterService(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, IMapper mapper)
    {
        m_userRepository = userRepository;
        m_bCryptPasswordEncoder = bCryptPasswordEncoder;
        m_mapper = mapper;
    }

    private User createUser(RegisterRequest registerRequest)
    {
        var user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(m_bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreated_at(LocalDateTime.now());
        user.setRoles(Roles.USER);

        return user;
    }
    public UserDTO register(RegisterRequest registerRequest)
    {
        if (m_userRepository.existsUserByUsername(registerRequest.getUsername()))
            throw new ApiException(MyError.USER_ALREADY_EXISTS);

        if (m_userRepository.existsUserByEmail(registerRequest.getEmail()))
            throw new ApiException(MyError.EMAIL_ALREADY_EXISTS);

        var user = createUser(registerRequest);
        m_userRepository.save(user);
        return m_mapper.toUserDTO(user);
    }
}
