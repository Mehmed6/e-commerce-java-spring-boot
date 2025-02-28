package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.jwtToken.JwtTokenDTO;
import com.doganmehmet.app.mapper.IJwtTokenMapper;
import com.doganmehmet.app.request.LoginRequest;
import com.doganmehmet.app.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController(JSONBeanName.JSON_LOGIN_CONTROLLER)
@RequestMapping("/my-login/json")
public class LoginController {

    private final LoginService m_loginService;
    private final IJwtTokenMapper m_jwtTokenMapper;

    public LoginController(LoginService loginService, IJwtTokenMapper jwtTokenMapper)
    {
        m_loginService = loginService;
        m_jwtTokenMapper = jwtTokenMapper;
    }

    @PostMapping()
    public JwtTokenDTO login(@Valid @RequestBody LoginRequest loginRequest)
    {
        return m_jwtTokenMapper.entityToDto(m_loginService.login(loginRequest));
    }
}
